package com.aneeshajose.headlines.displayarticles;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.aneeshajose.genericadapters.model.AbstractBetterViewHolder;
import com.aneeshajose.headlines.R;
import com.aneeshajose.headlines.models.Article;
import com.aneeshajose.headlines.utils.DateTimeUtils;
import com.bumptech.glide.RequestManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aneesha Jose on 2020-03-25.
 */
public class VH_Article extends AbstractBetterViewHolder<Article> {

    @BindView(R.id.ivProfile)
    ImageView ivProfile;

    @BindView(R.id.tvItemAuthor)
    TextView tvItemAuthor;

    @BindView(R.id.tvItemHeader)
    TextView tvItemHeader;

    @BindView(R.id.tvItemDesc)
    TextView tvItemDesc;

    @BindView(R.id.tvSource)
    TextView tvSource;

    @BindView(R.id.tvPublishedOn)
    TextView tvPublishedOn;

    @BindView(R.id.ivStar)
    ImageView ivStar;

    private Listener listener;
    private RequestManager glide;
    static final int LAYOUT = R.layout.item_article;

    VH_Article(View view, RequestManager glide, Listener listener) {
        super(view);
        this.glide = glide;
        this.listener = listener;
        ButterKnife.bind(this, view);
    }

    @Override
    public void bind(Article element, int position) {
        if (!TextUtils.isEmpty(element.getUrlToImage())) {
            glide.load(element.getUrlToImage())
                    .centerInside()
                    .placeholder(R.drawable.ic_face_grey)
                    .into(ivProfile);
        }
        tvItemAuthor.setText(tvItemAuthor.getContext().getString(R.string.by, getAppropriateString(element.getAuthor())));
        tvItemHeader.setText(getAppropriateString(element.getTitle()));
        tvItemDesc.setText(getAppropriateString(element.getDescription()));
        if (TextUtils.isEmpty(element.getPublishedAt()))
            tvPublishedOn.setText(R.string.NA);
        else
            tvPublishedOn.setText(DateTimeUtils.getFormattedDate(element.getPublishedAt(), DateTimeUtils.UTC_FORMAT, DateTimeUtils.USER_FRIENDLY_DATE));
        tvSource.setText(tvSource.getContext().getString(R.string.from, getAppropriateString(element.getSource() != null ? element.getSource().getName() : null)));

        ivStar.setOnClickListener(view -> {
            element.setSaved(!element.isSaved());
            listener.handleFavArticle(element, position);
        });
        setSelectionIndicator(element.isSaved());
    }

    void setSelectionIndicator(boolean isSelected) {
        ivStar.setImageDrawable(ContextCompat.getDrawable(ivStar.getContext(), isSelected ? R.drawable.ic_star : R.drawable.ic_star_border));
    }

    interface Listener {
        void handleFavArticle(Article element, int position);
    }

    private String getAppropriateString(String string) {
        return TextUtils.isEmpty(string) ? "-" : string;
    }
}
