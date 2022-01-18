package west2test;

import java.sql.*;

public class Release {

    /**
     * ȡ�����ݿ������
     * @return һ�����ݿ������
     */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            //��ʼ��������com.mysql.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/wea?characterEncoding=UTF-8","root", "Snake770315");
            //������� mysql-connector-java-5.0.8-bin.jar��,��������˵�һ������ĵ������ͻ��׳�ClassNotFoundException
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * ��װ�����رշ���
     * @param pstmt
     */
    public static void close(PreparedStatement pstmt){
        if(pstmt != null){						//������ֿ�ָ���쳣
            try{
                pstmt.close();
            }catch(SQLException e){
                e.printStackTrace();
            }

        }
    }

    public static void close(Connection conn){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}
