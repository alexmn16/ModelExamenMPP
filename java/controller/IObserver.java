package controller;


import model.Result;
import model.ResultDTO;

import java.util.List;

public interface IObserver{
    void resultAdded(List<ResultDTO> results) throws Exception;
}
