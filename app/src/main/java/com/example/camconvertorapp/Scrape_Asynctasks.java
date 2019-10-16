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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Scrape_Asynctasks extends AsyncTask<String, Integer, String>
{
    public static String imgurl = "";
    public static String price = "";

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

            Pattern pattern = Pattern.compile("data:image.*?'");
            Elements scripts = doc.select("script");
            for (Element script : scripts) {
                if (script.html().contains("data:image") && script.html().contains("dimg_"))
                {
                    Log.i("INSIDE", imgurl);
                    Matcher matcher = pattern.matcher(script.html());
                    if(matcher.find())
                    {
                        imgurl = script.html().substring(matcher.start(), matcher.end() - 1);
                        Log.i("REGEX", imgurl);
                        int len = imgurl.length();
                        Log.i("REGEXLEN", Integer.toString(len));
                        break;
                    }
                }
            }



            Elements links = doc.select(".rc");
            pattern = Pattern.compile("price[ ]*\\:?[ ]*[$|₪|€|£]?[ ]*\\d+[$|₪|€|£]?");


            for (Element link : links)
            {
                Log.i("Link", link.text());

                // no need the title only body
                Elements titles = link.select(".r");
                String title = titles.text();

                Elements bodies = link.select(".s");
                String body = bodies.text();
                String lowered = body.toLowerCase();
                Matcher matcher = pattern.matcher(lowered);
                if(matcher.find())
                {
                    Log.i("PRICE,1", lowered.substring(matcher.start(),matcher.end()));

                    price = lowered.substring(matcher.start(),matcher.end());
                    return "finished";
                }

                int start = lowered.indexOf("price");

                if (start != -1)
                {
                    Log.i("PRICE,2", body.substring(start, start+10));
                    price = body.substring(start, start+10);

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
