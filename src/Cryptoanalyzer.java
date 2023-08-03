import Controller.Controller;
import Controller.MyController;
import Model.Model;
import Model.MyModel;


public class Cryptoanalyzer {
    public static DAO.DAO dao;
    public static Controller controller;
    public static Model model;
    public static View view;

    //List of params to pass between View <-> Controller.Controller <-> Model.Model
    // 0 - input file
    // 1 - shift
    // 2 - output file
    // 3 - file with common words
    // 4 - file with example text
    // 5 - selected (being performed) operation
    // 6 - internal (from method) message

    public static String[] params = new String[7];

    public static void main(String[] args) {
        dao = new DAO.FileDAO();
        model = new MyModel(dao, params);
        controller = new MyController(dao, model);
        view = new MyView();
        view.letsStart();
    }
}