package com.example.android.bakingtime;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Step;
import com.example.android.bakingtime.utils.Constants;
import com.example.android.bakingtime.utils.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepDetailFragment extends Fragment {

    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView mIngredientsRecyclerview;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.tv_title_ingredient)
    TextView mIngredientTitleTextview;

    @BindView(R.id.steps_layout)
    LinearLayout mStepsLayout;

    @BindView(R.id.iv_thumbnailURL)
    ImageView mThumbnaulURLImageview;

    @BindView(R.id.tv_shortDescription)
    TextView mShortDescriptionTextview;

    @BindView(R.id.tv_description)
    TextView mDescriptionTextview;

    private Unbinder unbinder;

    private List<Ingredient> ingredients = new ArrayList<>();
    private Step step;

    private SimpleExoPlayer mExoPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

            if (getArguments() != null && getArguments().containsKey(Constants.INGREDIENTS_FOR_COOKING)) {
                ingredients = getArguments().getParcelableArrayList(Constants.INGREDIENTS_FOR_COOKING);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                mIngredientsRecyclerview.setLayoutManager(layoutManager);

                mIngredientsRecyclerview.setHasFixedSize(true);

                RecipeIngredientAdapter recipeIngredientAdapter = new RecipeIngredientAdapter(ingredients);
                mIngredientsRecyclerview.setAdapter(recipeIngredientAdapter);

                mIngredientTitleTextview.setVisibility(View.VISIBLE);
            } else if (getArguments() != null && getArguments().containsKey(Constants.STEP_FOR_COOKING)) {
                step = getArguments().getParcelable(Constants.STEP_FOR_COOKING);
                mStepsLayout.setVisibility(View.VISIBLE);

               pupulateUI(container);

            }

        return rootView;
    }

   public void pupulateUI(ViewGroup container){

       if(!step.getVideoURL().isEmpty()){
           if(NetworkUtils.isFileImage(step.getVideoURL())){
               Picasso.with(container.getContext())
                       .load(step.getVideoURL())
                       .error(R.drawable.sample_5)
                       .placeholder(R.drawable.sample_5)
                       .into(mThumbnaulURLImageview);
           }else {
               mPlayerView.setVisibility(View.VISIBLE);
               initializePlayer(Uri.parse(step.getVideoURL()));
           }
       }

       if(!step.getThumbnailURL().isEmpty()){
           if(NetworkUtils.isFileImage(step.getThumbnailURL())){
               Picasso.with(container.getContext())
                       .load(step.getThumbnailURL())
                       .error(R.drawable.sample_5)
                       .placeholder(R.drawable.sample_5)
                       .into(mThumbnaulURLImageview);
           }else {
               mPlayerView.setVisibility(View.VISIBLE);
               initializePlayer(Uri.parse(step.getThumbnailURL()));
           }
       }

       mShortDescriptionTextview.setText(step.getShortDescription());
       mDescriptionTextview.setText(step.getDescription());
   }

    public void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingTimeApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null) {
            releasePlayer();
        }
    }

}
