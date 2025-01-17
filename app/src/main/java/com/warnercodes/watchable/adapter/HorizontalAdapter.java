package com.warnercodes.watchable.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.warnercodes.watchable.Cast;
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.R;
import com.warnercodes.watchable.Review;
import com.warnercodes.watchable.activity.MovieDetailActivity;
import com.warnercodes.watchable.activity.ReviewActivity;
import com.warnercodes.watchable.databinding.ItemAdviceBinding;
import com.warnercodes.watchable.databinding.ItemCastBinding;
import com.warnercodes.watchable.databinding.ItemMovieBinding;
import com.warnercodes.watchable.databinding.ItemMovieGridBinding;
import com.warnercodes.watchable.databinding.ItemReviewBinding;
import com.warnercodes.watchable.databinding.ItemReviewCompleteBinding;
import com.warnercodes.watchable.databinding.ItemTrailerBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Movie> dataList;
    private List<Cast> castList;
    private List<Review> reviewList;
    private Review completeReview;
    private List<Movie> gridList;

    public HorizontalAdapter(Context context, List<Movie> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    public HorizontalAdapter(Context context) {
        this.context = context;
        castList = new ArrayList<Cast>();
        reviewList = new ArrayList<Review>();
        gridList = new ArrayList<Movie>();
    }


    public void add(int position, Movie item) {
        dataList.add(position, item);
        notifyItemInserted(position);
    }

    public void addCast(int position, Cast item) {
        castList.add(position, item);
        notifyItemInserted(position);
    }

    public void addReview(int position, Review item) {
        reviewList.add(position, item);
        notifyItemInserted(position);
    }

    public void addGrid(int position, Movie item) {
        gridList.add(position, item);
        notifyItemInserted(position);
    }

    public void clear() {
        if (gridList != null)
            gridList.clear();
        if (dataList != null)
            dataList.clear();
    }

    public void setCompleteReview(Review completeReview) {
        this.completeReview = completeReview;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    private static int RECENTI = 1;
    private static int CONSIGLIATI = 2;
    private static int CAST = 3;
    private static int REVIEW = 4;
    private static int COMPLETE_REVIEW = 5;
    private static int TRAILER = 6;
    private static int GRID = 7;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewBinding view;
        if (viewType == RECENTI) {
            view = ItemMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MovieViewHolder((ItemMovieBinding) view);
        }
        if (viewType == CONSIGLIATI) {
            view = ItemAdviceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new AdviceViewHolder((ItemAdviceBinding) view);
        }
        if (viewType == CAST) {
            view = ItemCastBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new CastViewHolder((ItemCastBinding) view);
        }
        if (viewType == REVIEW) {
            view = ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ReviewViewHolder((ItemReviewBinding) view);
        }
        if (viewType == COMPLETE_REVIEW) {
            view = ItemReviewCompleteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new CompleteReviewViewHolder((ItemReviewCompleteBinding) view);
        }
        if (viewType == TRAILER) {
            view = ItemTrailerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new TrailersViewHolder((ItemTrailerBinding) view);
        }
        if (viewType == GRID) {
            view = ItemMovieGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new GridViewHolder((ItemMovieGridBinding) view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == RECENTI) {
            MovieViewHolder viewHolder = (MovieViewHolder) holder;
            context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == GRID) {
            GridViewHolder viewHolder = (GridViewHolder) holder;
            context = viewHolder.img_movie.getContext();
            Glide.with(context).load(gridList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == CAST) {
            CastViewHolder viewHolder = (CastViewHolder) holder;
            context = viewHolder.attore.getContext();
            Cast itemCast = castList.get(position);
            Glide.with(context).load(itemCast.getProfile_path()).into(viewHolder.cover);
            viewHolder.attore.setText(itemCast.getName());
            viewHolder.personaggio.setText(itemCast.getCharacter());
        }

        if (getItemViewType(position) == CONSIGLIATI) {
            AdviceViewHolder viewHolder = (AdviceViewHolder) holder;
            context = viewHolder.img_advice.getContext();
            if (!((Activity) context).isFinishing())
                Glide.with(context).load(dataList.get(position).getBackdrop()).into(viewHolder.img_advice);
            TextView textView = viewHolder.title_advice;
            TextView textView1 = viewHolder.trama_adv;
            textView.setText(dataList.get(position).getTitle());
            textView1.setText(dataList.get(position).getTrama());
            textView1.setMovementMethod(new ScrollingMovementMethod());

            if (dataList.get(position).getGeneri() != null) {
                Log.i("GENERI", String.valueOf(dataList.get(position).getGeneri().size()));
                List<String> generi = new ArrayList<String>(dataList.get(position).getGeneri());

                for (String genere : generi) {
                    Chip chip = new Chip(context);
                    chip.setText(genere);
                    viewHolder.chipGroup.addView(chip);
                }
            }
        }
        if (getItemViewType(position) == REVIEW) {
            ReviewViewHolder viewHolder = (ReviewViewHolder) holder;
            viewHolder.reviewauthor.setText(reviewList.get(position).getAuthor());
            viewHolder.reviewContent.setText(reviewList.get(position).getText());
            viewHolder.score.setText(String.valueOf(reviewList.get(position).getScore()));
        }
        if (getItemViewType(position) == COMPLETE_REVIEW) {
            CompleteReviewViewHolder viewHolder = (CompleteReviewViewHolder) holder;
            String author = completeReview.getAuthor();
            Date date = completeReview.getDate();

            SimpleDateFormat formatter = new SimpleDateFormat(context.getString(R.string.dateformat));
            String format = formatter.format(date);
            viewHolder.author_date.setText(String.format("%s  %s", author, format));
            viewHolder.text.setText(completeReview.getText());
            viewHolder.score.setText(String.valueOf(completeReview.getScore()));
            viewHolder.title.setText(completeReview.getTitle());
            int totalvotes = completeReview.getVotedown() + completeReview.getVoteup();
            viewHolder.votes.setText(String.format(context.getString(R.string.helpful_review), completeReview.getVoteup(), totalvotes));

        }
        if (getItemViewType(position) == TRAILER) {
            TrailersViewHolder viewHolder = (TrailersViewHolder) holder;
            Movie item = dataList.get(position);
            if (!((Activity) context).isFinishing())
                Glide.with(context)
                        .load("https://img.youtube.com/vi/" + item.getYoutubekey() + "/maxresdefault.jpg")
                        .error(Glide.with(context)
                                .load("https://img.youtube.com/vi/" + item.getYoutubekey() + "/mqdefault.jpg"))
                        .into(viewHolder.cover);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (dataList != null) {
            String tipo = dataList.get(position).getTipo();
            if (tipo.equals("recenti"))
                return RECENTI;
            if (tipo.equals("consigliati"))
                return CONSIGLIATI;
            if (tipo.equals("cast"))
                return CAST;
            if (tipo.equals("trailer"))
                return TRAILER;
        } else if (reviewList != null && reviewList.size() > 0) {
            return REVIEW;
        } else if (castList != null && castList.size() > 0) {
            String tipo = castList.get(position).getTipo();
            if (tipo.equals("cast"))
                return CAST;
        } else if (gridList != null && gridList.size() > 0) {
            return GRID;
        } else if (completeReview != null) {
            return COMPLETE_REVIEW;
        }
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        if (dataList != null)
            return dataList.size();
        else if (castList != null && castList.size() > 0)
            return castList.size();
        else if (reviewList != null && reviewList.size() > 0)
            return reviewList.size();
        else if (completeReview != null)
            return 1;
        else if (gridList != null)
            return gridList.size();
        return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_movie;

        MovieViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.img_movie = binding.imgMovie;
            img_movie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer id = dataList.get(getAdapterPosition()).getMovieId();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("movieId", id);
                    context.startActivity(intent);
                }
            });
        }
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_movie;

        GridViewHolder(ItemMovieGridBinding binding) {
            super(binding.getRoot());
            this.img_movie = binding.imgMovie;
            img_movie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer id = gridList.get(getAdapterPosition()).getMovieId();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("movieId", id);
                    context.startActivity(intent);
                }
            });
        }
    }

    private class AdviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_advice;
        private TextView title_advice;
        private TextView trama_adv;
        private ChipGroup chipGroup;
        private ImageView add_watchlist;

        AdviceViewHolder(ItemAdviceBinding binding) {
            super(binding.getRoot());
            this.img_advice = binding.imgAdvice;
            this.title_advice = binding.titleAdvice;
            this.trama_adv = binding.tramaAdvice;
            this.chipGroup = binding.chipGroupGeneri;
            img_advice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer id = dataList.get(getAdapterPosition()).getMovieId();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("movieId", id);
                    context.startActivity(intent);
                }
            });
            this.add_watchlist = binding.icon;
            this.add_watchlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    SharedPreferences sharedPref = context.getSharedPreferences("infos", Context.MODE_PRIVATE);
                    String uid = sharedPref.getString("uid", null);
                    DocumentReference user = db.collection("utenti").document(uid);
                    Movie movieRef = dataList.get(getAdapterPosition());
                    DocumentReference watchlist = user.collection("watchlist").document(String.valueOf(movieRef.getMovieId()));

                    Map<String, Object> movieInfos = new HashMap<>();
                    movieInfos.put("title", movieRef.getTitle());
                    movieInfos.put("movieId", movieRef.getMovieId());
                    movieInfos.put("copertina", movieRef.getCopertina());
                    movieInfos.put("runtime", movieRef.getRuntime());
                    watchlist.set(movieInfos).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dataList.remove(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        }

    }

    private class CastViewHolder extends RecyclerView.ViewHolder {
        private TextView attore;
        private TextView personaggio;
        private ImageView cover;
        private CardView cardView;

        CastViewHolder(ItemCastBinding binding) {
            super(binding.getRoot());
            this.attore = binding.personName;
            this.personaggio = binding.characterName;
            this.cover = binding.profilePath;
            this.cardView = binding.castCardview;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cast cast = castList.get(getAdapterPosition());
                    Log.i("CAST", cast.toString());
                }
            });
        }
    }

    private class ReviewViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView reviewContent;
        private TextView reviewauthor;
        private TextView score;

        ReviewViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.reviewContent = binding.content;
            this.reviewauthor = binding.author;
            this.score = binding.imdbScore;
            this.cardView = binding.cardviewReview;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Review review = reviewList.get(getAdapterPosition());
                    Intent intent = new Intent(context, ReviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("review", review);
                    context.startActivity(intent);
                }
            });
        }
    }

    private class CompleteReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        private TextView author_date;
        private TextView score;
        private TextView title;
        private TextView votes;

        CompleteReviewViewHolder(ItemReviewCompleteBinding binding) {
            super(binding.getRoot());
            this.text = binding.content;
            this.author_date = binding.authorDate;
            this.score = binding.imdbScore;
            this.title = binding.reviewTitle;
            this.votes = binding.votes;
        }
    }

    private class TrailersViewHolder extends RecyclerView.ViewHolder {
        private ImageView cover;
        private TextView title;
        private MaterialCardView cardView;

        public TrailersViewHolder(ItemTrailerBinding binding) {
            super(binding.getRoot());
            this.cover = binding.youtubeThumbnail;
            this.cardView = binding.card;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = dataList.get(getAdapterPosition()).getYoutubekey();
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + key));
                    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        context.startActivity(webIntent);
                    }
                }
            });
        }
    }
}
