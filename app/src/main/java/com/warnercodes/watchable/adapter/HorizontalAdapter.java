package com.warnercodes.watchable.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.warnercodes.watchable.Cast;
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.MovieDetailActivity;
import com.warnercodes.watchable.Review;
import com.warnercodes.watchable.ReviewActivity;
import com.warnercodes.watchable.databinding.ItemAdviceBinding;
import com.warnercodes.watchable.databinding.ItemCastBinding;
import com.warnercodes.watchable.databinding.ItemRecentBinding;
import com.warnercodes.watchable.databinding.ItemReviewBinding;
import com.warnercodes.watchable.databinding.ItemReviewCompleteBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Movie> dataList;
    private List<Cast> castList;
    private List<Review> reviewList;
    private Review completeReview;

    public HorizontalAdapter(Context context, List<Movie> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    public HorizontalAdapter(Context context) {
        this.context = context;
        castList = new ArrayList<Cast>();
        reviewList = new ArrayList<Review>();
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

    public void remove(Movie item) {
        int position = dataList.indexOf(item);
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public void setCompleteReview(Review completeReview) {
        this.completeReview = completeReview;
    }

    private static int RECENTI = 1;
    private static int CONSIGLIATI = 2;
    private static int CAST = 3;
    private static int REVIEW = 4;
    private static int COMPLETE_REVIEW = 5;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewBinding view;
        if (viewType == RECENTI) {
            view = ItemRecentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new RecentiViewHolder((ItemRecentBinding) view);
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == RECENTI) {
            RecentiViewHolder viewHolder = (RecentiViewHolder) holder;
            context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
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
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_advice);
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
            viewHolder.author_date.setText(author + "  " + date);
            viewHolder.text.setText(completeReview.getText());
            viewHolder.score.setText(String.valueOf(completeReview.getScore()));
            viewHolder.title.setText(completeReview.getTitle());
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
            if (tipo.equals("simili"))
                return RECENTI;
            if (tipo.equals("cinema"))
                return RECENTI;
            if (tipo.equals("popolari"))
                return RECENTI;
            if (tipo.equals("arrivo"))
                return RECENTI;
            if (tipo.equals("votati"))
                return RECENTI;
            if (tipo.equals("cast"))
                return CAST;
        } else if (reviewList != null && reviewList.size() > 0) {
            return REVIEW;
        } else if (castList != null && castList.size() > 0) {
            String tipo = castList.get(position).getTipo();
            if (tipo.equals("cast"))
                return CAST;
        } else {
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
        else
            return 1;
    }


    class RecentiViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_movie;

        RecentiViewHolder(ItemRecentBinding binding) {
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

    class AdviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_advice;
        private TextView title_advice;
        private TextView trama_adv;
        private ChipGroup chipGroup;

        AdviceViewHolder(ItemAdviceBinding binding) {
            super(binding.getRoot());
            this.img_advice = binding.imgAdvice;
            this.title_advice = binding.titleAdvice;
            this.trama_adv = binding.tramaAdvice;
            this.chipGroup = binding.chipGroupGeneri;
        }

    }


    class CastViewHolder extends RecyclerView.ViewHolder {
        private TextView attore;
        private TextView personaggio;
        private ImageView cover;

        CastViewHolder(ItemCastBinding binding) {
            super(binding.getRoot());
            this.attore = binding.personName;
            this.personaggio = binding.characterName;
            this.cover = binding.profilePath;
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
                    intent.putExtra("review", (Serializable) review);
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

        CompleteReviewViewHolder(ItemReviewCompleteBinding binding) {
            super(binding.getRoot());
            this.text = binding.content;
            this.author_date = binding.authorDate;
            this.score = binding.imdbScore;
            this.title = binding.reviewTitle;
        }
    }
}
