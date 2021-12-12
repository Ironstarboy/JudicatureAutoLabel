package DataSci.judicature.service;

import DataSci.judicature.domain.CaseMarksArr;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface WordService {

    CaseMarksArr extract(String fileName, String type) throws IOException;
}
