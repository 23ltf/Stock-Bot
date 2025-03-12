package stockTrading;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Portfolio extends Component {

    private ArrayList<StockData> myPortfolio;

    public Portfolio() {
        myPortfolio = new ArrayList<>();
        // Load saved portfolio state if needed
    }

    public void addStck(StockData stockInfo) {
        myPortfolio.add(stockInfo);
        for (int i = 0; i < myPortfolio.size(); i++)
            System.out.println(myPortfolio.get(i));

    }
    public void updtstck(String tic, double cP, double pr5DaA, double pr1MoA, double pr1YrA, double per, double bta, double diYi) {
        for (StockData stock : myPortfolio) {
            if (stock.getTic().equals(tic)) {
                // Update the stock information
                stock.setCurrentPrice(cP);
                stock.setPrice5DaysAgo(pr5DaA);
                stock.setPrice1MonthAgo(pr1MoA);
                stock.setPrice1YearAgo(pr1YrA);
                stock.setPeRatio(per);
                stock.setBeta(bta);
                stock.setDividendYield(diYi);
                return;
            }
        }
    }

    public void removeStock(String ticker) {
        boolean removed = myPortfolio.removeIf(stock -> stock.getTic().equalsIgnoreCase(ticker));
        if (removed) {
            JOptionPane.showMessageDialog(this, "Stock removed from portfolio.");
        } else {
            JOptionPane.showMessageDialog(this, "Stock not found in portfolio.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /*
    public void containsStock(String ticker) {
        for (int i = 0; i < myPortfolio.size()) {
            if (stock.getTicker().equalsIgnoreCase(ticker)) {
                myPortfolio.remove(i);
            }
        }
        return false;
    }

*/
    public StockData getStckByTic(String ticker) {
        for (StockData stock : myPortfolio) {
            if (stock.getTic().equalsIgnoreCase(ticker)) {
                return stock;
            }
        }
        return null;
    }
    public ArrayList<StockData> getPortfolio() {
        return myPortfolio;
    }

    public void saveState() {
        try {
            PrintWriter output = new PrintWriter("saveState.txt");
            for (StockData stock : myPortfolio) {
                output.println(stock); // Assuming StockData class has a suitable toString() method
            }
            output.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Couldn't save");
        }
    }
}

