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

package com.aurora.adroid.task;

import android.content.Context;
import android.content.ContextWrapper;

import com.aurora.adroid.database.AppDao;
import com.aurora.adroid.database.AppDatabase;
import com.aurora.adroid.database.PackageDao;

public class DatabaseTask extends ContextWrapper {

    private Context context;

    public DatabaseTask(Context context) {
        super(context);
        this.context = context;
    }

    public boolean clearAllTables() {
        try {
            final AppDatabase appDatabase = AppDatabase.getAppDatabase(context);
            appDatabase.clearAllTables();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clearRepo(String repoID) {
        try {
            final AppDatabase appDatabase = AppDatabase.getAppDatabase(context);
            final AppDao appDao = appDatabase.appDao();
            appDao.clearRepo(repoID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
