/*
 * Aurora Droid
 * Copyright (C) 2019, Rahul Kumar Patel <whyorean@gmail.com>
 *
 * Aurora Droid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aurora Droid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aurora Droid.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aurora.adroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurora.adroid.R;
import com.aurora.adroid.Sort;
import com.aurora.adroid.activity.AuroraActivity;
import com.aurora.adroid.adapter.GenericAppsAdapter;
import com.aurora.adroid.task.FetchAppsTask;
import com.aurora.adroid.util.Log;
import com.aurora.adroid.util.ViewUtil;
import com.aurora.adroid.view.CustomSwipeToRefresh;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GenericAppsFragment extends Fragment {

    @BindView(R.id.swipe_layout)
    CustomSwipeToRefresh customSwipeToRefresh;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.sort_name_az)
    Chip chipNameAZ;
    @BindView(R.id.sort_name_za)
    Chip chipNameZA;
    @BindView(R.id.sort_size_min)
    Chip chipSizeMin;
    @BindView(R.id.sort_size_max)
    Chip chipSizeMax;
    @BindView(R.id.sort_date_updated)
    Chip chipDateUpdated;
    @BindView(R.id.sort_date_added)
    Chip chipDateAdded;
    @BindView(R.id.chip_group)
    ChipGroup chipGroup;

    private Context context;
    private ActionBar actionBar;
    private int listType;
    private BottomNavigationView bottomNavigationView;
    private GenericAppsAdapter genericAppsAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generic_apps, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecycler();
        setupChip();
        Bundle arguments = getArguments();
        if (arguments != null) {
            listType = arguments.getInt("LIST_TYPE", 0);
            if (listType == 0) {
                fetchNewAppsData();
                chipGroup.check(R.id.sort_date_added);
            } else {
                fetchUpdatedAppsData();
                chipGroup.check(R.id.sort_date_updated);
            }
        }
        if (getActivity() instanceof AuroraActivity) {
            bottomNavigationView = ((AuroraActivity) getActivity()).getBottomNavigationView();
            actionBar = ((AuroraActivity) getActivity()).getDroidActionBar();
            ViewUtil.hideBottomNav(bottomNavigationView, true);
            if (listType == 0)
                actionBar.setTitle(getString(R.string.title_apps_new));
            else

                actionBar.setTitle(getString(R.string.title_latest_updated));
        }
    }

    @Override
    public void onDestroy() {
        ViewUtil.showBottomNav(bottomNavigationView, true);
        actionBar.setTitle(R.string.title_home);
        disposable.clear();
        super.onDestroy();
    }

    private void setupChip() {
        if (genericAppsAdapter != null) {
            chipNameAZ.setOnClickListener(v -> genericAppsAdapter.sortBy(Sort.NAME_AZ));
            chipNameZA.setOnClickListener(v -> genericAppsAdapter.sortBy(Sort.NAME_ZA));
            chipSizeMax.setVisibility(View.GONE);
            chipSizeMin.setVisibility(View.GONE);
            chipDateUpdated.setOnClickListener(v -> genericAppsAdapter.sortBy(Sort.DATE_UPDATED));
            chipDateAdded.setOnClickListener(v -> genericAppsAdapter.sortBy(Sort.DATE_ADDED));
        }
    }

    private void fetchUpdatedAppsData() {
        disposable.add(Observable.fromCallable(() -> new FetchAppsTask(context)
                .getLatestUpdatedApps(15))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> customSwipeToRefresh.setRefreshing(true))
                .doOnTerminate(() -> customSwipeToRefresh.setRefreshing(false))
                .subscribe((appList) -> {
                    if (!appList.isEmpty()) {
                        genericAppsAdapter.addData(appList);
                        genericAppsAdapter.sortBy(Sort.DATE_UPDATED);
                    }
                }, err -> {
                    Log.e(err.getMessage());
                    err.printStackTrace();
                }));
    }

    private void fetchNewAppsData() {
        disposable.add(Observable.fromCallable(() -> new FetchAppsTask(context)
                .getLatestAddedApps(15))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> customSwipeToRefresh.setRefreshing(true))
                .doOnTerminate(() -> customSwipeToRefresh.setRefreshing(false))
                .subscribe((appList) -> {
                    if (!appList.isEmpty()) {
                        genericAppsAdapter.addData(appList);
                        genericAppsAdapter.sortBy(Sort.DATE_ADDED);
                    }
                }, err -> {
                    Log.e(err.getMessage());
                    err.printStackTrace();
                }));
    }

    private void setupRecycler() {
        genericAppsAdapter = new GenericAppsAdapter(context);
        recyclerView.setAdapter(genericAppsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(context, R.anim.anim_falldown));
    }
}