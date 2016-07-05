package ak.scrabble.engine;

import ak.scrabble.conf.ScrabbleDbConf;
import ak.scrabble.engine.da.BaseDAO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by akopylov on 06.11.2015.
 */
@ContextConfiguration(classes={ScrabbleDbConf.class})
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractDBTest {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseDAO.class);

    private NamedParameterJdbcOperations jdbc;
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public NamedParameterJdbcOperations getJdbc() {
        return jdbc;
    }
}
