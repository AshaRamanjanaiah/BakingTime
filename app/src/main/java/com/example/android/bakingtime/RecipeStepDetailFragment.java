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
import com.google.android.exoplayer2.C;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepDetailFragment extends Fragment {

    private static final String PLAYER_POSITION = "position";
    private static final String IS_PLAYER_READY = "is_player_ready";
    private static final String PLAYER_WINDOW = "window";
    private static final String RECIPE_STEP = "recipe_step";

    private long mStartPosition;
    private boolean mIsPlayerReady;
    private int mStartWindow;

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

    private Step step;

    private SimpleExoPlayer mExoPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null){
            mStartPosition = savedInstanceState.getInt(PLAYER_POSITION);
            mIsPlayerReady = savedInstanceState.getBoolean(IS_PLAYER_READY);
            mStartWindow = savedInstanceState.getInt(PLAYER_WINDOW);
            step = savedInstanceState.getParcelable(RECIPE_STEP);
        }else {
            clearStartPosition();
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        List<Ingredient> ingredients;

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

   private void pupulateUI(ViewGroup container){

       if(!step.getVideoURL().isEmpty()){
           if(NetworkUtils.isFileImage(step.getVideoURL())){
               Picasso.with(container.getContext())
                       .load(step.getVideoURL())
                       .error(R.drawable.sample_5)
                       .placeholder(R.drawable.sample_5)
                       .into(mThumbnaulURLImageview);
           }else {
               mPlayerView.setVisibility(View.VISIBLE);
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
           }
       }

       mShortDescriptionTextview.setText(step.getShortDescription());
       mDescriptionTextview.setText(step.getDescription());
   }

    private void initializePlayer(Uri mediaUri) {
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
            boolean haveStartPosition = mStartWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                mExoPlayer.seekTo(mStartWindow, mStartPosition);
            }
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mIsPlayerReady);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if ((mIngredientTitleTextview.getVisibility() == View.GONE) && !step.getVideoURL().equals("") && Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(step.getVideoURL()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIngredientTitleTextview.getVisibility() == View.GONE && !step.getVideoURL().equals("") && (Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer(Uri.parse(step.getVideoURL()));
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
        if (mIngredientTitleTextview.getVisibility() == View.GONE && !step.getVideoURL().equals("") && Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mIngredientTitleTextview.getVisibility() == View.GONE && !step.getVideoURL().equals("")) {
            mStartPosition = mExoPlayer.getCurrentPosition();
            mIsPlayerReady = mExoPlayer.getPlayWhenReady();
            mStartWindow = mExoPlayer.getCurrentWindowIndex();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(PLAYER_POSITION, (int) mStartPosition);
        outState.putBoolean(IS_PLAYER_READY, mIsPlayerReady);
        outState.putInt(PLAYER_WINDOW, mStartWindow);
        outState.putParcelable(RECIPE_STEP, step);
        super.onSaveInstanceState(outState);
    }

    private void clearStartPosition() {
        mIsPlayerReady = true;
        mStartPosition = C.TIME_UNSET;
        mStartWindow = C.INDEX_UNSET;
    }
}
