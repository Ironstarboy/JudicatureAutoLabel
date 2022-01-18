package DataSci.judicature.service;

import DataSci.judicature.domain.CaseMarks;
import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.domain.CaseMsg;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface WordService {

    CaseMarksArr extract(String fileName, HttpSession session) throws IOException;

    CaseMarks toJSON(CaseMsg caseMsg);
}
