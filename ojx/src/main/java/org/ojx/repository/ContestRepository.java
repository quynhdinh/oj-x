package org.ojx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.ojx.connection.ConnectionManager;
import org.ojx.dto.ContestDTO;
import org.ojx.model.Contest;

public class ContestRepository {
    private static final String TABLE_NAME = "contest";
    private static final Logger log = Logger.getLogger(UserRepository.class.getName());

    public List<Contest> getAll() throws SQLException {
        List<Contest> contests = new ArrayList<>();
         // Assuming you have a method to get a connection
         try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT contest_id, contest_name, length, started_at, problem_ids, points FROM " + TABLE_NAME;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int contestId = rs.getInt("contest_id");
                String contestName = rs.getString("contest_name");
                int length = rs.getInt("length");
                long startedAt = rs.getLong("started_at");
                String problemIds = rs.getString("problem_ids");
                String points = rs.getString("points");
                contests.add(new Contest(contestId, contestName, length, startedAt, problemIds, points));
            }
        } catch (SQLException e) {
            log.warning("Error fetching contests: " + e.getMessage());
        }
        return contests;
    }

    public int create(ContestDTO contest){
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO " + TABLE_NAME + " (contest_name, length, started_at, problem_ids, points) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, contest.contestName());
            pstmt.setInt(2, contest.length());
            pstmt.setLong(3, contest.startedAt());
            pstmt.setString(4, contest.problemIds());
            pstmt.setString(5, contest.points());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Optional<Contest> getContestById(int contestId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE contest_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, contestId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("contest_id");
                String contestName = rs.getString("contest_name");
                int length = rs.getInt("length");
                long startedAt = rs.getLong("start_at");
                String problemIds = rs.getString("problem_ids");
                String points = rs.getString("points");
                return Optional.of(new Contest(id, contestName, length, startedAt, problemIds, points));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
