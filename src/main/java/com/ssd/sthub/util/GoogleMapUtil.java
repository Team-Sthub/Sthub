package com.ssd.sthub.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleMapUtil {

    @Value("${google.map-api.secret-key}")
    private static String API_KEY;

    public static Map<String, String> getGeoDataByAddress(String completeAddress) {
        try {
            String surl = "https://maps.googleapis.com/maps/api/geocode/json?address="+ URLEncoder.encode(completeAddress, "UTF-8")+"&key="+API_KEY;
            URL url = new URL(surl);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
//            log.info(">>>>>>>>>> >>>>>>>>>> InputStream Start <<<<<<<<<< <<<<<<<<<<");
            while ((inputStr = streamReader.readLine()) != null) {
//                log.info(">>>>>>>>>>     "+inputStr);
                responseStrBuilder.append(inputStr);
            }
//            log.info(">>>>>>>>>> >>>>>>>>>> InputStream End <<<<<<<<<< <<<<<<<<<<");

            JSONObject jo = new JSONObject(responseStrBuilder.toString());

            JSONArray results = jo.getJSONArray("results");
            Map<String, String> ret = new HashMap<String, String>();

            if(results.length() > 0) {
                JSONObject jsonObject;
                jsonObject = results.getJSONObject(0);

                Double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                ret.put("lat", lat.toString());
                ret.put("lng", lng.toString());

                System.out.println("LAT:\t\t"+lat);
                System.out.println("LNG:\t\t"+lng);
                JSONArray ja = jsonObject.getJSONArray("address_components");

                for(int l=0; l<ja.length(); l++) {
                    JSONObject curjo = ja.getJSONObject(l);
                    String type = curjo.getJSONArray("types").getString(0);
                    String short_name = curjo.getString("short_name");

                    if(type.equals("postal_code")) {
                        log.info("POSTAL_CODE: "+short_name);
                        ret.put("zip", short_name);
                    }
                    else if(type.equals("administrative_area_level_3")) {
                        log.info("CITY: "+short_name);
                        ret.put("city", short_name);
                    }
                    else if(type.equals("administrative_area_level_2")) {
                        log.info("PROVINCE: "+short_name);
                        ret.put("province", short_name);
                    }
                    else if(type.equals("administrative_area_level_1")) {
                        log.info("REGION: "+short_name);
                        ret.put("region", short_name);
                    }
                }
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
