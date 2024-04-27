package poly.manhnt.datn_md09.ConnectInternet;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DownloadJson extends AsyncTask<String, Void, String> {
    String duongdan;
    List<HashMap<String, String>> attrs;
    StringBuilder dulieu;
    boolean method = true;
    public  DownloadJson (String duongdan){
        this.duongdan = duongdan;
        method = true;
    }
    public DownloadJson(String duongdan, List<HashMap<String, String>> attrs){
        this.duongdan = duongdan;
        this.attrs = attrs;
        method = false;

    }
    @Override
    protected String doInBackground(String... strings) {
        String data = "";
        try {
            URL url = new URL(duongdan);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if(!method){
                data = methodPost(httpURLConnection);
            }else {
                data = methodGet(httpURLConnection);
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    private String methodGet(HttpURLConnection httpURLConnection){
        String data = "";
        InputStream inputStream = null;
        try {
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            dulieu = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) !=null){
                dulieu.append(line);
            }
            data = dulieu.toString();
            bufferedReader.close();
            reader.close();
            inputStream.close();
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
        return data;
    }
    private String methodPost(HttpURLConnection httpURLConnection){
        String data = "";
        String key = "";
        String value = "";
        try {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            Uri.Builder builder = new Uri.Builder();
            int count = attrs.size();
            for (int i=0; i<count; i++){
                for(Map.Entry<String, String> values: attrs.get(i).entrySet()) {
                    key = values.getKey();
                    value = values.getValue();
                }
                builder.appendQueryParameter(key, value);
            }
            String query = builder.build().getEncodedQuery();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(query);
            writer.flush();
            bufferedWriter.close();
            writer.close();
            outputStream.close();

            data = methodGet(httpURLConnection);

        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }
}
