package ak.scrabble.engine.da;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by akopylov on 05/07/16.
 */
@Repository
public class BaseDAO {
    protected static final Logger LOG = LoggerFactory.getLogger(BaseDAO.class);

    protected NamedParameterJdbcOperations jdbc;
    private DataSource dataSource;
    private Connection connection;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public Connection getConnection() throws SQLException {
        this.connection = connection != null ? connection : dataSource.getConnection();
        return connection;
    }

}
