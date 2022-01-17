package org.powertac.weatherserver.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileDataSeeder implements DataSeeder {

    @Value("${data.seed.file}")
    private String seedFile;

    private final SeedStatusRepository seedStatusRepository;
    private final JdbcTemplate jdbc;
    private final Logger logger;

    public FileDataSeeder(SeedStatusRepository seedStatusRepository, JdbcTemplate jdbc) {
        this.seedStatusRepository = seedStatusRepository;
        this.jdbc = jdbc;
        this.logger = LogManager.getLogger(FileDataSeeder.class);
    }

    @Override
    @Transactional
    public void seed() {
        try {
            logger.info(String.format("importing seed file %s", seedFile));
            Path seedFilePath = Paths.get(seedFile);
            if (!Files.exists(seedFilePath)) {
                logger.warn(String.format("file seed %s does not exist; skipping import", seedFile));
                return;
            }
            String seed = Files.readString(seedFilePath);
            String hash = md5(seed);
            if (seedStatusRepository.existsById(hash)) {
                logger.info(String.format("skipping already imported seed %s", seedFile));
                return;
            }
            persistSeed(seed);
            seedStatusRepository.save(new SeedStatus(hash, Instant.now()));
            logger.info(String.format("imported data seed %s", seedFile));
        } catch (IOException e) {
            logger.error(String.format("failed to import seed file %s", seedFile), e);
        } catch (NoSuchAlgorithmException e) {
            logger.error(String.format("failed to create hash for seed file %s", seedFile), e);
        } catch (DataAccessException e) {
            logger.error(String.format("could not check seed status for file %s", seedFile), e);
        } catch (Exception e) {
            logger.error(String.format("seed import failed for file %s", seedFile), e);
        }
    }

    private String md5(String seed) throws NoSuchAlgorithmException {
        byte[] messageBytes = seed.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = MessageDigest.getInstance("MD5").digest(messageBytes);
        return DatatypeConverter.printHexBinary(md5Bytes);
    }

    private void persistSeed(String seed) {
        List<String> statements = parseStatements(seed);
        int i = 0;
        for (String statement : statements) {
            i++;
            if (i % 10 == 0) {
                double progress = (double) i / (double) statements.size();
                logger.info(String.format("running statement %d of %d (%d%%)", i, statements.size(), (int) (progress * 100)));
            }
            jdbc.execute(statement);
        }
    }

    private List<String> parseStatements(String batch) {
        List<String> statements = new ArrayList<>();
        ScriptUtils.splitSqlScript(batch, ';', statements);
        return statements;
    }

}
