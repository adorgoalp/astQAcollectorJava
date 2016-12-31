

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHelper {
	private final static String QA_TABLE = "QA_TABLE";
	private final static String META_DATA_TABLE = "android_metadata";
	static String url = "jdbc:sqlite:";
	

	public DatabaseHelper(String dbName) {
		url += dbName;
	}

	private Connection connect() {
		Connection connection = null;
			try {
				connection = DriverManager.getConnection(url);
			} catch (SQLException e) {
				System.out.println(">>");
				System.out.println(e.getMessage());
			
		}
		return connection;
	}

	public static void createNewDatabase() {
		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public void prepareDatabaseForLocalImport()
	{
		String sqlDrop = "DROP TABLE IF EXISTS "+META_DATA_TABLE+";";
		String sql = "CREATE TABLE IF NOT EXISTS "
				+META_DATA_TABLE+"(locale TEXT DEFAULT 'en_US');";
		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
//			stmt.executeQuery(sqlDrop);
			stmt.execute(sql);
//			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		String insertSQL = "INSERT INTO "+META_DATA_TABLE+" VALUES (?);";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
			stmt.setString(1, "en_US");
			stmt.execute();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void createQATable() {
		String sqlDrop = "DROP TABLE IF EXISTS "+QA_TABLE+";";
		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS "
				+QA_TABLE+" (" 
				+ "qaId number PRIMARY KEY," 
				+ "question text,"
				+ "answer text," 
				+ "category text);";

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
			// stmt.executeQuery(sqlDrop);
			stmt.execute(sql);
			System.out.println("Table created");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertAll(ArrayList<QuestionAnswerModel> result) {
		if(result == null)
		{
			System.err.println("Null result");
			return;
		}
		for(int i = 0 ; i < result.size() ; i++)
		{
			QuestionAnswerModel qa = result.get(i);
			insert(qa.questionId,qa.question,qa.answer,qa.category);
		}
	}

	private void insert(int questionId, String question, String answer, String category) {
		String sql = "INSERT INTO "+QA_TABLE+" (qaId,question,answer,category) VALUES(?,?,?,?)";
		 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, questionId);
            pstmt.setString(2, question);
            pstmt.setString(3, answer);
            pstmt.setString(4, category);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
}
