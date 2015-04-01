
package com.dropbox.android.Mobiquity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;

public class ShowList extends AsyncTask<Void, Long, Boolean> {

    private Context mContext;
    private DropboxAPI<?> mApi;
    private String mPath;
    private ImageView mView;
    private boolean mCanceled;
    private String mErrorMsg;
    ArrayList<String> dir = null;
    String[] fnames = null;
    int i = 0;
    ListView myListView = null;
    public ShowList(Context context, DropboxAPI<?> api,
                    String dropboxPath, ImageView view) {

        mContext = context.getApplicationContext();
        mApi = api;
        mPath = dropboxPath;
        mView = view;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (mCanceled) {
                return false;
            }
            // Getting the metadata for a directory
            Entry dirent = mApi.metadata(mPath, 1000, null, true, null);
            if (!dirent.isDir || dirent.contents == null) {
                // It's not a directory, or there's nothing in it
                mErrorMsg = "File or empty directory";
                return false;
            }
            ArrayList<Entry> files = new ArrayList<Entry>();
            ArrayList<String> dir = new ArrayList<String>();
            for (Entry ent : dirent.contents) {
                files.add(ent);
                dir.add(new String(files.get(i++).path));
            }
            i = 0;
            fnames = dir.toArray(new String[dir.size()]);
            final ArrayAdapter<String> aa;
            aa = new ArrayAdapter<String>(mContext, R.layout.activity_list_item, dir);
            myListView.setAdapter(aa);
            Log.i("a", myListView.getAdapter().toString());
        } catch (DropboxException e) {
            // Unknown error
            mErrorMsg = "Unknown error.  Try again.";
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(Long... progress) {
    }
    @Override
    protected void onPostExecute(Boolean result) {
    }

}
