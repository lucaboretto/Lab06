package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public String getUmiditaMedie(int mese){
		
		String umidita="";
		
		final String sql = "SELECT Localita, AVG(Umidita) AS u "
				+ "FROM situazione "
				+ "WHERE MONTH(DATA) = ? "
				+ "GROUP BY Localita";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, mese);
			ResultSet rs = st.executeQuery();
			
			
			while (rs.next()) {
				umidita += rs.getString("Localita");
				umidita += " " + rs.getString("u") + "\n";
			}

			conn.close();
			return umidita;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
		
	
	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		List<Rilevamento> rilevamentiByLocalitaMese = new ArrayList<>();
		
				final String sql = "SELECT Localita, Umidita, Data "
						+ "FROM situazione "
						+ "WHERE MONTH(DATA) = ? "
						+ "AND Localita = ?";
				
				try {
					Connection conn = ConnectDB.getConnection();
					PreparedStatement st = conn.prepareStatement(sql);

					st.setInt(1, mese);
					st.setString(2, localita);
					ResultSet rs = st.executeQuery();
					
					
					while (rs.next()) {
						Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
						rilevamentiByLocalitaMese.add(r);
					}

					conn.close();
					return rilevamentiByLocalitaMese;

				} catch (SQLException e) {

					e.printStackTrace();
					throw new RuntimeException(e);
				}
	}


}
