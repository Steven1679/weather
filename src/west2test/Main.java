package west2test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class Main {
    public static void main(String[] args) {
        //get location
        String fz = getData("https://geoapi.qweather.com/v2/city/lookup?key=73f8645abe4c407b8367a4687777296f&location=fuzhou&number=1");
        ArrayList<City> fz1 = jsonLoc(fz);
        City FZ = fz1.get(0);
        String bj = getData("https://geoapi.qweather.com/v2/city/lookup?key=73f8645abe4c407b8367a4687777296f&location=beijing&number=1");
        ArrayList<City> bj1 = jsonLoc(bj);
        City BJ = bj1.get(0);
        String sh = getData("https://geoapi.qweather.com/v2/city/lookup?key=73f8645abe4c407b8367a4687777296f&location=shanghai&number=1");
        ArrayList<City> sh1 = jsonLoc(sh);
        City SH = sh1.get(0);
        //get weather situation
        String fzWea = getData("https://devapi.qweather.com/v7/weather/3d?key=73f8645abe4c407b8367a4687777296f&location=" + FZ.getId());
        ArrayList<Wea> fzWea1 = jsonWea(fzWea);
        String bjWea = getData("https://devapi.qweather.com/v7/weather/3d?key=73f8645abe4c407b8367a4687777296f&location=" + BJ.getId());
        ArrayList<Wea> bjWea1 = jsonWea(bjWea);
        String shWea = getData("https://devapi.qweather.com/v7/weather/3d?key=73f8645abe4c407b8367a4687777296f&location=" + SH.getId());
        ArrayList<Wea> shWea1 = jsonWea(shWea);
        Scanner sc = new Scanner(System.in);
        //loop input
        while (true) {
            System.out.println("You need to refresh database(input 2) at first, otherwise you will only get old data");
            System.out.println("input 1 for selecting city to print weather\ninput 2 for refreshing the information of weather \ninput exit for ending");
            String n = sc.nextLine();
            if (n.equals("exit")) {
                break;
            } else {
                if (n.equals("1")) {
                    //startpages means page index(start from 1), rows means number of lines per page
                    System.out.println("input city name(Chinese),startpage,rows to get three days weather");
                    String city = sc.next();
                    int startpage=sc.nextInt();
                    int rows=sc.nextInt();
                    List<CityWea> list=findWea(city,startpage,rows);
                    for (CityWea cityWea : list) {
                        System.out.println(cityWea.toString());
                    }

                } else if (n.equals("2")) {
                   updateWeather(FZ,BJ,SH,fzWea1,bjWea1,shWea1);
                    System.out.println("refresh success");
                }
                else{
                    System.out.println("invalid input");
                }
            }
        }


    }

    //process json of location
    public static ArrayList jsonLoc(String json) {
        JSONObject object = JSON.parseObject(json);
        JSONArray array = object.getJSONArray("location");
        ArrayList<City> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            City city = new City();
            JSONObject object1 = array.getJSONObject(i);
            city.setName(object1.getString("name"));//city name
            city.setId(object1.getString("id"));// city id
            city.setLat(object1.getString("lat"));// latitude
            city.setLon(object1.getString("lon"));// lontitude
            list.add(city);
        }
        return list;
    }

    //process json of weather
    public static ArrayList jsonWea(String json) {
        JSONObject object = JSON.parseObject(json);
        JSONArray array = object.getJSONArray("daily");
        ArrayList<Wea> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            Wea wea = new Wea();
            JSONObject object1 = array.getJSONObject(i);
            wea.setFxDate(object1.getString("fxDate"));//date
            wea.setTempMax(object1.getString("tempMax"));// max temperature
            wea.setTempMin(object1.getString("tempMin"));// min temperature
            wea.setTextDay(object1.getString("textDay"));// the weather situation of day
            list.add(wea);
        }
        return list;
    }

    //call the api
    public static String getData(String link) {
        URL url;
        String result = null;
        try {
            url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream is = connection.getInputStream();
            GZIPInputStream gzipInputStream = new GZIPInputStream(is);
            StringBuilder res = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null) {
                res.append(line);
            }
            result = res.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * update weather information
     * delete the original data then insert new data
     * avoid duplicate weather records on the same date
     */
    public static void updateWeather(City FZ, City BJ, City SH, ArrayList<Wea> fz, ArrayList<Wea> bj, ArrayList<Wea> sh) {
        //delete the whole table
        del();
        //initialize location database
        addLoc(FZ.getId(), FZ.getName(), FZ.getLat(), FZ.getLon());
        addLoc(BJ.getId(), BJ.getName(), BJ.getLat(), BJ.getLon());
        addLoc(SH.getId(), SH.getName(), SH.getLat(), SH.getLon());
        //initialize weather database
        for (int i = 0; i < 3; i++) {
            addWea(FZ.getId(), FZ.getName(), fz.get(i).getFxDate(), fz.get(i).getTempMax(), fz.get(i).getTempMin(), fz.get(i).getTextDay());
            addWea(BJ.getId(), BJ.getName(), bj.get(i).getFxDate(), bj.get(i).getTempMax(), bj.get(i).getTempMin(), bj.get(i).getTextDay());
            addWea(SH.getId(), SH.getName(), sh.get(i).getFxDate(), sh.get(i).getTempMax(), sh.get(i).getTempMin(), sh.get(i).getTextDay());
        }

    }

    /**
     * delete the database
     */
    public static void del() {
        String sql = "delete from location";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = Release.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            Release.close(pstmt);
            Release.close(conn);
        }
    }


    /**
     * add information of weather
     */
    public static void addWea(String id, String name, String fxDate, String Max, String Min, String Day) {
        String sql = "insert into weather(id,name,fxDate,Max,Min,Day) values(?,?,?,?,?,?)";
        Connection conn = null;                //connect with database
        PreparedStatement pstmt = null;        //create statement
        try {
            conn = Release.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id); //assignment
            pstmt.setString(2, name);
            pstmt.setString(3, fxDate);
            pstmt.setString(4, Max);
            pstmt.setString(5, Min);
            pstmt.setString(6, Day);
            pstmt.executeUpdate();            //execute
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Release.close(pstmt);
            Release.close(conn);        //close
        }
    }

    /**
     * add information of location
     */
    public static void addLoc(String id, String name, String lat, String lon) {
        String sql = "insert into location(id,name,lat,lon) values(?,?,?,?)";
        Connection conn = null;                //connect with database
        PreparedStatement pstmt = null;        //create statement
        try {
            conn = Release.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id); //assignment
            pstmt.setString(2, name);
            pstmt.setString(3, lat);
            pstmt.setString(4, lon);
            pstmt.executeUpdate();            //execute
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Release.close(pstmt);
            Release.close(conn);        //close
        }
    }


    /**
     * search three days weathers of one city
     */
    public static List<CityWea> findWea(String cityname,int startpage,int rows) {
        String sql = "select l.*,w.fxDate,w.Max,w.Min,w.Day from location as l,weather as w where l.id=w.id and l.name=? limit ?,?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        //create a list to store the data
        List<CityWea> cityList = new ArrayList<>();
        try {
            conn = Release.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cityname);
            //implement select by page
            int start = (startpage-1)*rows;
            pstmt.setInt(2, start);
            pstmt.setInt(3, rows);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String lat = rs.getString("lat");
                String lon = rs.getString("lon");
                String fxDate = rs.getString("fxDate");
                String Max = rs.getString("Max");
                String Min = rs.getString("Min");
                String Day = rs.getString("Day");
                //every record correspon with a object
                CityWea city = new CityWea();
                city.setId(id);
                city.setName(name);
                city.setLat(lat);
                city.setLon(lon);
                city.setFxDate(fxDate);
                city.setMax(Max);
                city.setMin(Min);
                city.setDay(Day);
                //put the object into set
                cityList.add(city);
            }
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            Release.close(pstmt);
            Release.close(conn);        //close
        }
        return cityList;
    }
}
