package com.aneeshajose.headlines.displayarticles;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aneeshajose.genericadapters.SimpleGenericAdapterBuilder;
import com.aneeshajose.genericadapters.SimpleGenericRecyclerViewAdapter;
import com.aneeshajose.headlines.DependencyInjector;
import com.aneeshajose.headlines.R;
import com.aneeshajose.headlines.base.activity.BaseActivity;
import com.aneeshajose.headlines.base.contract.BaseView;
import com.aneeshajose.headlines.models.Article;
import com.aneeshajose.headlines.webview.WebViewActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class DisplayNewsArticlesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, DisplayArticleContract.View, VH_Article.Listener {


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.shimmerLayout)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.rvRepoList)
    RecyclerView rvRepoList;

    @Inject
    public DisplayArticlePresenterImp presenter;
    public List<Article> masterList = new ArrayList<>();
    public List<String> publishers = null;
    private SimpleGenericRecyclerViewAdapter<Article> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_articles);
        initAdapter();
        initView();
        swipeRefresh.setOnRefreshListener(this);
    }

    private void initAdapter() {
        adapter = new SimpleGenericAdapterBuilder<>(context, Article.class, VH_Article.LAYOUT, view -> new VH_Article(view, glide, this))
                .setClickable(true)
                .setOnItemClicked((position, item) -> startWebViewActivity((Article) item))
                .buildRecyclerViewAdapter();
        rvRepoList.setLayoutManager(new LinearLayoutManager(this));
        rvRepoList.setAdapter(adapter);
    }

    private void startWebViewActivity(Article item) {
        startActivity(WebViewActivity.createInstance(context, item.getUrl(), item.getTitle()));
        overridePendingTransition(R.anim.stay, R.anim.enter_from_right);
    }

    private void initView() {
        startShimmer();
        presenter.getArticles(this);
    }

    @Override
    protected BaseView getBaseView() {
        return this;
    }

    @Override
    protected void callDependencyInjector(DependencyInjector injectorComponent) {
        injectorComponent.injectDependencies(this);
    }

    @Override
    protected void handleRetryClick() {
        swipeRefresh.setRefreshing(true);
        onRefresh();
    }

    private void stopLoading() {
        swipeRefresh.setRefreshing(false);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        rvRepoList.setVisibility(View.VISIBLE);
    }

    private void startShimmer() {
        rvRepoList.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onRefresh() {
        initView();
    }

    @Override
    public void onArticlesLoaded(List<Article> articles) {
        if (articles.isEmpty()) {
            showError(getString(R.string.oops), getString(R.string.no_relevant_data_found), true);
            return;
        }
        masterList.clear();
        masterList.addAll(articles);
        adapter.resetAllItems(masterList);
        if (publishers != null)
            publishers = presenter.getListPublishers(masterList);
    }

    private void filterByPublisher(String publisher) {
        adapter.publishItems(presenter.getFilteredArticles(masterList, publisher));
    }

    @Override
    public void updateSavedStatus(int position, boolean saved) {
        adapter.get(position).setSaved(saved);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void showProgress() {
        startShimmer();
    }

    @Override
    public void hideProgress() {
        stopLoading();
    }

    @Override
    public void showMessage(String toastMessage, boolean showErrorView) {
        if (showErrorView)
            showError(toastMessage, true);
        else
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleFavArticle(Article element, int position) {
        presenter.manageArticle(element, position);
        adapter.notifyItemChanged(position);
    }

    @OnClick(R.id.tvFilter)
    void onFilterClicked() {
        if (publishers == null)
            publishers = presenter.getListPublishers(masterList);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, publishers), (dialogInterface, i) -> {
            filterByPublisher(publishers.get(i));
            dialogInterface.dismiss();
        });
        builder.create().show();
    }

    @OnClick(R.id.tvSort)
    void onSortClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] values = getResources().getStringArray(R.array.sort_values);
        builder.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, values), (dialogInterface, i) -> {
            adapter.publishItems(presenter.sortArticles(adapter.getAll(), values[i].equals(getString(R.string.old_to_new))));
            dialogInterface.dismiss();
        });
        builder.create().show();
    }
}
