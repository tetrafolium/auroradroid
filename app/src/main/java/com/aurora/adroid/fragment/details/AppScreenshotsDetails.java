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

package com.aurora.adroid.fragment.details;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurora.adroid.R;
import com.aurora.adroid.adapter.SmallScreenshotsAdapter;
import com.aurora.adroid.fragment.DetailsFragment;
import com.aurora.adroid.model.App;
import com.aurora.adroid.util.DatabaseUtil;

import butterknife.BindView;

public class AppScreenshotsDetails extends AbstractDetails {

    @BindView(R.id.screenshots_gallery)
    RecyclerView recyclerView;

    public AppScreenshotsDetails(DetailsFragment fragment, App app) {
        super(fragment, app);
    }

    @Override
    public void draw() {
        if (app.getScreenShots() != null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new SmallScreenshotsAdapter(DatabaseUtil.getScreenshotURLs(app), context));
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        }
    }
}
