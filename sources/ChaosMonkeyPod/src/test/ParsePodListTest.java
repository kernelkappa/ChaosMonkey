import it.poznanski.chaosmonkeypod.rest.client.K8SRestClientException;
import it.poznanski.chaosmonkeypod.utils.CommonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class ParsePodListTest {

    private static String podListText;
    @Before
    public void initTest() throws IOException{
        Path path = Paths.get("src/test/resources/podList.json");
        List<String> lines = Files.readAllLines(path);
        StringBuffer buffer = new StringBuffer();
        for(String line:lines){
            buffer.append(line);
        }
        podListText = buffer.toString();
    }

    @Test
    public void parsePodList() throws K8SRestClientException {
        ArrayList<String> pods = CommonUtils.parsePodList(podListText);
        assert(pods.size() != 0);
    }
}
