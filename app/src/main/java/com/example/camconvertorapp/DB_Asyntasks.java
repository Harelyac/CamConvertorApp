package com.example.camconvertorapp;
import android.os.AsyncTask;



/**this class consists of asyntask classes manage insertion and deletion of the
 * newFrequency object typed by the user - into the local Room App data base**/


class insertLocalAsyncTask extends AsyncTask<Frequency,Void,Void> {
    private FreqDao mAsyntaskDao;
    private boolean fromDefault;


    insertLocalAsyncTask(FreqDao dao, boolean fromDefault)
    {
        this.mAsyntaskDao = dao;
        this.fromDefault = fromDefault;
    }


    // this part is because the full stack junior didn't know that update query existed in sql
    @Override
    protected Void doInBackground(Frequency... frequencies) {
        // get the old frequency
        Frequency old_frequency = mAsyntaskDao.findByType(frequencies[0].type);


        // if there is no frequency yet
        if (old_frequency == null)
        {
            // the first insert is always from default
            mAsyntaskDao.insertAll(frequencies[0]);
        }

        // if there is frequency in the room
        else
        {
            if (old_frequency.target.equals("Select an item...") && old_frequency.source.equals("Select an item..."))
            {
                mAsyntaskDao.delete(old_frequency);
                mAsyntaskDao.insertAll(frequencies[0]);
            }

            // if one of them has valid value
            else
            {
                // only the user can insert new ones
                if (!fromDefault)
                {
                    mAsyntaskDao.delete(old_frequency);
                    mAsyntaskDao.insertAll(frequencies[0]);
                }
            }
        }
        return null;
    }
}


class deleteALLAsyncTask extends AsyncTask<Frequency,Void,Void>{
    private FreqDao mAsyntaskDao;

    deleteALLAsyncTask(FreqDao dao){
        this.mAsyntaskDao = dao;
    }

    @Override
    protected Void doInBackground(Frequency... msgs) {
        mAsyntaskDao.deleteAll();
        return null;
    }
}




