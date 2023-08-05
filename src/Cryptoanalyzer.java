import Controller.Controller;
import Controller.MyController;
import Model.Model;
import Model.MyModel;
import View.View;
import View.MyView;


public class Cryptoanalyzer {
    //List of String[] params to exchange data between View.View <-> Controller.Controller <-> Model.Model
    // 0 - input file
    // 1 - shift
    // 2 - output file
    // 3 - file with common words
    // 4 - file with example text
    // 5 - selected (being performed) operation
    // 6 - internal (from method) message

    public static void main(String[] args) {
        String[] params = new String[7];
        DAO.DAO dao = new DAO.FileDAO();
        Model model = new MyModel(dao, params);
        Controller controller = new MyController(dao, model, params);
        View view = new MyView();
        view.letsStart(controller, params);
    }
}