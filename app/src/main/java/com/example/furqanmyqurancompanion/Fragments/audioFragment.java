package com.example.furqanmyqurancompanion.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Adapters.AudioSurahAdapter;
import com.example.furqanmyqurancompanion.Api.Api_Client;
import com.example.furqanmyqurancompanion.Api.QuranAPiService;
import com.example.furqanmyqurancompanion.Model.Ayah_Data;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.Model.Reciter;
import com.example.furqanmyqurancompanion.Model.ReciterResponse;
import com.example.furqanmyqurancompanion.Model.SurahContentResponse;
import com.example.furqanmyqurancompanion.Model.Surah_Data;
import com.example.furqanmyqurancompanion.Model.Surah_Metadata;
import com.example.furqanmyqurancompanion.PlaybackService;
import com.example.furqanmyqurancompanion.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class audioFragment extends Fragment {

    private AutoCompleteTextView reciterDropdown;
    private RecyclerView rvAudioSurahs;
    private CardView playerControlCard;
    private TextView tvNowPlaying;
    private ImageButton btnPlayPause;
    private SeekBar playerSeekBar;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;

    private List<Reciter> reciterList = new ArrayList<>();
    private String selectedReciterIdentifier = "ar.alafasy"; // Default
    private MediaController mediaController;
    private ListenableFuture<MediaController> controllerFuture;
    private final android.os.Handler progressHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final Runnable updateProgressAction = this::updateProgress;

    public audioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupReciters();
        setupSurahs();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeController();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaController != null) {
            mediaController.release();
            mediaController = null;
        }
        if (controllerFuture != null) {
            MediaController.releaseFuture(controllerFuture);
            controllerFuture = null;
        }
    }

    private void initViews(View view) {
        reciterDropdown = view.findViewById(R.id.reciterDropdown);
        rvAudioSurahs = view.findViewById(R.id.rvAudioSurahs);
        playerControlCard = view.findViewById(R.id.playerControlCard);
        tvNowPlaying = view.findViewById(R.id.tvNowPlaying);
        btnPlayPause = view.findViewById(R.id.btnPlayPause);
        playerSeekBar = view.findViewById(R.id.playerSeekBar);
        tvCurrentTime = view.findViewById(R.id.tvCurrentTime);
        tvTotalTime = view.findViewById(R.id.tvTotalTime);

        btnPlayPause.setOnClickListener(v -> {
            if (mediaController != null) {
                if (mediaController.isPlaying()) {
                    mediaController.pause();
                } else {
                    mediaController.play();
                }
            }
        });

        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaController != null) {
                    mediaController.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progressHandler.removeCallbacks(updateProgressAction);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressHandler.post(updateProgressAction);
            }
        });
    }

    private void initializeController() {
        Context context = requireContext();
        SessionToken sessionToken = new SessionToken(context, new ComponentName(context, PlaybackService.class));
        controllerFuture = new MediaController.Builder(context, sessionToken).buildAsync();
        controllerFuture.addListener(() -> {
            try {
                mediaController = controllerFuture.get();
                updatePlayerUi();
                mediaController.addListener(new Player.Listener() {
                    @Override
                    public void onIsPlayingChanged(boolean isPlaying) {
                        updatePlayPauseIcon(isPlaying);
                        if (isPlaying) {
                            progressHandler.post(updateProgressAction);
                        } else {
                            progressHandler.removeCallbacks(updateProgressAction);
                        }
                    }

                    @Override
                    public void onPlaybackStateChanged(int playbackState) {
                        if (playbackState == Player.STATE_READY) {
                            playerSeekBar.setMax((int) mediaController.getDuration());
                            tvTotalTime.setText(formatTime((int) mediaController.getDuration()));
                            progressHandler.post(updateProgressAction);
                        }
                    }

                    @Override
                    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                        if (mediaItem != null) {
                            tvNowPlaying.setText("Now Playing: " + mediaItem.mediaMetadata.title);
                            playerSeekBar.setProgress(0);
                            tvCurrentTime.setText("0:00");
                        }
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
    }

    private void updateProgress() {
        if (mediaController != null && mediaController.isPlaying()) {
            long currentPosition = mediaController.getCurrentPosition();
            playerSeekBar.setProgress((int) currentPosition);
            tvCurrentTime.setText(formatTime((int) currentPosition));
            progressHandler.postDelayed(updateProgressAction, 1000);
        }
    }

    private String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        int hours = (millis / (1000 * 60 * 60));

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }

    private void updatePlayerUi() {
        if (mediaController != null && mediaController.getCurrentMediaItem() != null) {
            playerControlCard.setVisibility(View.VISIBLE);
            tvNowPlaying.setText("Now Playing: " + mediaController.getCurrentMediaItem().mediaMetadata.title);
            updatePlayPauseIcon(mediaController.isPlaying());
            
            playerSeekBar.setMax((int) mediaController.getDuration());
            playerSeekBar.setProgress((int) mediaController.getCurrentPosition());
            tvTotalTime.setText(formatTime((int) mediaController.getDuration()));
            tvCurrentTime.setText(formatTime((int) mediaController.getCurrentPosition()));
            
            if (mediaController.isPlaying()) {
                progressHandler.post(updateProgressAction);
            }
        }
    }

    private void updatePlayPauseIcon(boolean isPlaying) {
        btnPlayPause.setImageResource(isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
    }

    private void setupReciters() {
        QuranAPiService apiService = Api_Client.getQuranApiService();
        apiService.getReciters().enqueue(new Callback<ReciterResponse>() {
            @Override
            public void onResponse(Call<ReciterResponse> call, Response<ReciterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reciterList = response.body().getData();
                    List<String> reciterNames = new ArrayList<>();
                    for (Reciter r : reciterList) {
                        reciterNames.add(r.getEnglishName());
                    }
                    if (getContext() != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, reciterNames);
                        reciterDropdown.setAdapter(adapter);
                        reciterDropdown.setOnItemClickListener((parent, view, position, id) -> {
                            selectedReciterIdentifier = reciterList.get(position).getIdentifier();
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ReciterResponse> call, Throwable t) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "Failed to load reciters", Toast.LENGTH_SHORT).show();
            }
        });

        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaController != null) {
                    mediaController.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progressHandler.removeCallbacks(updateProgressAction);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressHandler.post(updateProgressAction);
            }
        });
    }

    private void setupSurahs() {
        MyApplication app = (MyApplication) requireActivity().getApplicationContext();
        List<Surah_Metadata> surahs = app.getSurahs_metadata();
        AudioSurahAdapter adapter = new AudioSurahAdapter(surahs, this::playSurah);
        rvAudioSurahs.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAudioSurahs.setAdapter(adapter);
    }

    private void playSurah(Surah_Metadata surah) {
        if (mediaController == null) return;

        // Use a local copy to avoid issues if selectedReciterIdentifier changes during network call
        final String currentReciter = selectedReciterIdentifier;

        Toast.makeText(getContext(), "Loading audio...", Toast.LENGTH_SHORT).show();
        QuranAPiService apiService = Api_Client.getQuranApiService();
        apiService.getSurahWithAudio(surah.getSurah_number(), currentReciter).enqueue(new Callback<SurahContentResponse>() {
            @Override
            public void onResponse(Call<SurahContentResponse> call, Response<SurahContentResponse> response) {
                if (!isAdded()) return; // Fragment not attached

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Surah_Data> editions = response.body().getData();
                    // Audio edition is typically at index 2, but let's verify if possible or at least check bounds
                    if (editions.size() >= 3) {
                        Surah_Data audioEdition = editions.get(2);
                        List<MediaItem> mediaItems = new ArrayList<>();
                        for (Ayah_Data ayah : audioEdition.getAyahs()) {
                            if (ayah.getAudioUrl() != null && !ayah.getAudioUrl().isEmpty()) {
                                mediaItems.add(new MediaItem.Builder()
                                        .setUri(ayah.getAudioUrl())
                                        .setMediaId(String.valueOf(ayah.getGlobalVerseNumber()))
                                        .setMediaMetadata(new androidx.media3.common.MediaMetadata.Builder()
                                                .setTitle(surah.getSurah_english_name())
                                                .setArtist(currentReciter)
                                                .build())
                                        .build());
                            }
                        }
                        
                        if (!mediaItems.isEmpty()) {
                            mediaController.setMediaItems(mediaItems);
                            mediaController.prepare();
                            mediaController.play();
                            playerControlCard.setVisibility(View.VISIBLE);
                            tvNowPlaying.setText("Now Playing: " + surah.getSurah_english_name());
                        } else {
                            Toast.makeText(getContext(), "No audio available for this surah", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SurahContentResponse> call, Throwable t) {
                if (isAdded() && getContext() != null)
                    Toast.makeText(getContext(), "Failed to load audio: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaController != null) {
                    mediaController.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progressHandler.removeCallbacks(updateProgressAction);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressHandler.post(updateProgressAction);
            }
        });
    }
}
