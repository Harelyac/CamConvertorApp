package com.example.camconvertorapp;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Scrape_Asynctasks extends AsyncTask<String, Integer, String>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try
        {
            Log.i("IN", "ASYNC");

            final Document doc = Jsoup
                .connect(strings[0])
                .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                .timeout(5000).get();

            // gets first image
            Elements images = doc.select("img[id^=dimg]");
            for (Element image : images)
            {
                Log.i("IMAGE", image.text());
            }

            Element image = images.first();
            String url = image.absUrl("src");
            Log.i("URL", url);

            // \bprice\b.*\d+ FIXME use regex

            Elements links = doc.select(".rc");

            for (Element link : links)
            {
                Log.i("Link", link.text());

                // no need the title only body
                Elements titles = link.select(".r");
                String title = titles.text();

                Elements bodies = link.select(".s");
                String body = bodies.text();
                String lowered = body.toLowerCase();
                int start = lowered.indexOf("price");

                if (start != -1)
                {
                    Log.i("PRICE", body.substring(start+6, start+10));
                }

                Log.i("Title: ", title + "\n");
                Log.i("Body: ", body);
            }
        }

        catch (IOException e)
        {
            Log.i("ERROR", "ASYNC");
        }
        return "finished";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
