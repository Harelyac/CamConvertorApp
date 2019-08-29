package com.example.camconvertorapp;

import android.os.AsyncTask;

import javax.xml.transform.Source;

/**this class consists of asyntask classes manage insertion and deletion of the
 * newFrequency object typed by the user - into the local Room App data base**/


    class insertLocalAsyncTask extends AsyncTask<Frequency,Void,Void> {
        private FreqDao mAsyntaskDao;


        insertLocalAsyncTask(FreqDao dao){
            this.mAsyntaskDao = dao;

        }

        // this part is because hazan didn't knw that update query existed in sql
        @Override
        protected Void doInBackground(Frequency... frequencies) {
            Frequency frequency =  mAsyntaskDao.findByType(frequencies[0].type);

            //first check if part of the fields of previous stored frequency have to be re- stored
            if ((frequencies[0].target == null || frequencies[0].source == null) && frequency != null)
            {
                if (frequencies[0].target == null ){
                    frequencies[0].target = frequency.target;
                }
                if (frequencies[0].source == null ){
                    frequencies[0].source = frequency.source;
                }
            }

            //replace the old frequency if exists with the new frequency
            if (frequency != null){
                mAsyntaskDao.delete(frequency);
                mAsyntaskDao.insertAll(frequencies[0]);

            }
            else {
                mAsyntaskDao.insertAll(frequencies[0]);
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


    //TODO add more asyntasks for converting on background a specific source type which has been -
    //TODO recognized by Camera - according to the stored target type which corresponds to the relevant type

   //use FreqDao.getAll() for saving the frequencies chosen by the user at global place in project
    //if some frequency's types were not set by the user (maybe all of them) - use DEFAULT types
