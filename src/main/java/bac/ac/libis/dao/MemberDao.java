package bac.ac.libis.dao;

import bac.ac.libis.model.Member;
import bac.ac.libis.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MemberDao {

    private static final String SELECT =
            "SELECT member_id, first_name, last_name, email, password FROM members";

    // read!

    public List<Member> findAll() throws SQLException {
        return query(SELECT + " ORDER BY member_id");
    }

    /** is member number the same as member id? (ask sir) */
    public Member findById(int memberId) throws SQLException {
        return first(query(SELECT + " WHERE member_id = ?", memberId));
    }

    public List<Member> findByLastName(String lastName) throws SQLException
    {
        return query(SELECT + " WHERE LOWER(last_name) = LOWER(?) ORDER BY member_id", lastName);
    }

    public Member findByEmail(String email) throws SQLException {
        return first(query(SELECT + " WHERE LOWER(email) = LOWER(?)", email));
    }

    /** Matches part names or id */
    public List<Member> search(String term) throws SQLException {
        String like = "%" + term.toLowerCase() + "%";
        return query(SELECT + " WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?"
                        + " OR LOWER(CONCAT(first_name, ' ', last_name)) LIKE ?"
                        + " OR CAST(member_id AS CHAR) = ? ORDER BY member_id",
                like, like, like, term.trim());
    }

    // write

    public int create(Member m) throws SQLException {
        String sql = "INSERT INTO members (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getFirstName());
            ps.setString(2, m.getLastName());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getPassword());
            ps.executeUpdate();


            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    m.setMemberId(keys.getInt(1));
                }
            }
        }
        return m.getMemberId();
    }

    /** returns true if a row was changed */
    public boolean update(Member m) throws SQLException {
        String sql = "UPDATE members SET first_name = ?, last_name = ?, email = ?, password = ? WHERE member_id = ?";
        return execute(sql, m.getFirstName(), m.getLastName(), m.getEmail(), m.getPassword(),
                m.getMemberId()) > 0;
    }

    /** returns true if a member was deleted */
    public boolean delete(int memberId) throws SQLException {
        return execute("DELETE FROM members WHERE member_id = ?", memberId) > 0;
    }


    private List<Member> query(String sql, Object... params) throws SQLException {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    members.add(mapRow(rs));
                }
            }
        }
        return members;
    }

    private int execute(String sql, Object... params) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        }
    }

    private Member first(List<Member> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("member_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password"));
    }
}