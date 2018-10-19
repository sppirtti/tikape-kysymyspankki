/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Vastaus;

/**
 *
 * @author Samuli
 */
public class VastausDao implements Dao<Vastaus, Integer> {

    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE id = ?");
        stmt.setInt(1, key);
        
        ResultSet rs = stmt.executeQuery();
        
        if(!rs.next()) {
            return null;
        }
        
        Vastaus vast = new Vastaus(rs.getInt("id"), rs.getInt("kysymys_id"), rs.getString("vastausteksti"), rs.getBoolean("oikein"));
        
        stmt.close();
        rs.close();
        conn.close();
        
        return vast;
        
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        List<Vastaus> vastaukset = new ArrayList<>();

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus");

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Vastaus v = new Vastaus(rs.getInt("id"), rs.getInt("kysymys_id"), rs.getString("vastausteksti"), rs.getBoolean("oikein"));
            vastaukset.add(v);
        }
        stmt.close();
        rs.close();
        conn.close();

        return vastaukset;

    }

    @Override
    public Vastaus save(Vastaus vastaus) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus (kysymys_id, vastausteksti ,oikein) VALUES (?, ?, ?)");
        stmt.setInt(1, vastaus.getKysymys_id());
        stmt.setString(2, vastaus.getVastausteksti());
        stmt.setBoolean(3, vastaus.getOikein());
        stmt.executeUpdate();

        stmt.close();

        conn.close();

        return null;
       
    }

    @Override
    public void delete(Integer key) throws SQLException {
       
        Connection conn = database.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE id = ?");
        stmt.setInt(1, key);
        
        stmt.executeUpdate();
        
        conn.close();
        
    }
    
    public List<Vastaus> findKysymys (Integer kysymysid) throws SQLException {
        List <Vastaus> vastaukset = new ArrayList<>();
        
        Connection conn = database.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE kysymys_id = ?");
        
        stmt.setInt(1, kysymysid);
        
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            vastaukset.add(new Vastaus (rs.getInt("id"), rs.getInt("kysymys_id"), rs.getString("vastausteksti"), rs.getBoolean("oikein")));
        }
        
        stmt.close();
        rs.close();
        conn.close();
        return vastaukset;
                
                
                
    }
}
