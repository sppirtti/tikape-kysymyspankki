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
import java.util.HashMap;
import java.util.List;
import tikape.runko.domain.Kysymys;
import tikape.runko.domain.Vastaus;

/**
 *
 * @author Samuli
 */
public class KysymysDao implements Dao<Kysymys, Integer> {

    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }

    @Override
    public Kysymys findOne(Integer key) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }
        Kysymys k = new Kysymys(rs.getInt("id"), rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymysteksti"));

        stmt.close();
        rs.close();
        conn.close();

        return k;

    }

    @Override
    public List<Kysymys> findAll() throws SQLException {
        List<Kysymys> kysymykset = new ArrayList<>();

        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Kysymys k = new Kysymys(rs.getInt("id"), rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymysteksti"));
            kysymykset.add(k);
        }
        stmt.close();
        rs.close();
        conn.close();

        return kysymykset;
    }

    @Override
    public Kysymys save(Kysymys kysymys) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (kurssi,aihe,kysymysteksti) VALUES (?, ?, ?)");
        stmt.setString(1, kysymys.getKurssi());
        stmt.setString(2, kysymys.getAihe());
        stmt.setString(3, kysymys.getKysymysteksti());
        stmt.executeUpdate();

        conn.close();

        return null;

    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmtvast = conn.prepareStatement("DELETE FROM Vastaus WHERE kysymys_id = ?");
        stmtvast.setInt(1, key);

        stmtvast.executeUpdate();

        stmtvast.close();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);

        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

}
