package com.example.camconvertorapp;
import android.os.AsyncTask;


/**this class consists of asyntask classes manage insertion and deletion of the
 * newFrequency object typed by the user - into the local Room App data base**/


class insertLocalAsyncTask extends AsyncTask<Frequency,Void,Void> {
    private FreqDao mAsyntaskDao;


    insertLocalAsyncTask(FreqDao dao) {
        this.mAsyntaskDao = dao;

    }

    // this part is because the full stack junior didn't know that update query existed in sql
    @Override
    protected Void doInBackground(Frequency... frequencies) {
        Frequency frequency = mAsyntaskDao.findByType(frequencies[0].type);

        //first check if part of the fields of previous stored frequency have to be re- stored
        if ((frequencies[0].target == null || frequencies[0].source == null) && frequency != null) {
            if (frequencies[0].target == null) {
                frequencies[0].target = frequency.target;
            }
            if (frequencies[0].source == null) {
                frequencies[0].source = frequency.source;
            }
        }

        //replace the old frequency if exists with the new frequency
        if (frequency != null) {
            mAsyntaskDao.delete(frequency);
            mAsyntaskDao.insertAll(frequencies[0]);

        } else {
            if ((frequencies[0].target == null || frequencies[0].source == null) && frequency == null) {
                return null;
            } else {
                mAsyntaskDao.insertAll(frequencies[0]);
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




