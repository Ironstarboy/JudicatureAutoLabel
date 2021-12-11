package DataSci.judicature.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface WordService {

    void extract(String fileName) throws IOException;
}
