package stockTrading;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    //declare layout
    private CardLayout cardLayout;
    //declare panels
    private JPanel menuPanel;
    private JPanel investigateStockPanel;
    private JPanel viewStockPanel;
    private JPanel tickerStockPanel;

    //declare jtextfield variables that help the bot decide if buy, sell, or hold
    private JTextField tickerField;
    private JTextField currentPriceField;
    private JTextField price5DaysAgoField;
    private JTextField price1MonthAgoField;
    private JTextField price1YearAgoField;
    private JTextField peRatioField;
    private JTextField betaField;
    private JTextField dividendYieldField;
    private JLabel decisionLabel;

    //background image
    private Image backgroundImage = new ImageIcon("img.png").getImage();

    //font and colour for buttons
    private Font buttonFont = new Font("Arial", Font.BOLD, 16);
    private Color fontColor = new Color(0, 102, 204);

    //declare objects
    private Portfolio portfolio;
    private Border emptyBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    private FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
    public Main() {
        //set size and title
        setTitle("Advanced Trading Bot");
        // Set frame size using Dimension object
        Dimension frameSize = new Dimension(500, 400);
        setSize(frameSize);
        setLocationRelativeTo(null); // Center the frame

        // Initialize objects
        portfolio = new Portfolio();

        //Intialize layout
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        //Initialize panels
        menuPanel = createMenuPanel();
        viewStockPanel = viewStocks();
        tickerStockPanel = createTickerStockPanel();

        //create panels
        getContentPane().add(menuPanel, "Menu");
        getContentPane().add(tickerStockPanel, "TickerStock");
        getContentPane().add(viewStockPanel, "ViewStock");
    }

    private JPanel createMenuPanel() {
        // Set the background to the previously declared image
        BackgroundPanel menuPanel = new BackgroundPanel(backgroundImage);

        // Set layout of menu panel to BoxLayout with Y_AXIS alignment
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        menuPanel.setBorder(emptyBorder);

        // Add glue component to push buttons to the top
        menuPanel.add(Box.createVerticalGlue());

        // Create and style investigate button
        JPanel investigateButtonPanel = new JPanel();
        investigateButtonPanel.setLayout(flowLayout);
        MenuButtons investigateStockButton = new MenuButtons("Investigate Stock", new Color(70, 130, 180), 20);
        investigateStockButton.setFont(buttonFont);
        investigateButtonPanel.setOpaque(false);
        investigateStockButton.setForeground(fontColor);
        investigateStockButton.addActionListener(e -> cardLayout.show(getContentPane(), "TickerStock"));
        investigateButtonPanel.add(investigateStockButton);
        menuPanel.add(investigateButtonPanel);

        // Create and style view stock list button
        JPanel viewButtonPanel = new JPanel();
        viewButtonPanel.setLayout(flowLayout);
        MenuButtons viewStockButton = new MenuButtons("View Stock List", new Color(70, 130, 180), 20);
        viewStockButton.setFont(buttonFont);
        viewButtonPanel.setOpaque(false);
        viewStockButton.setForeground(fontColor);
        viewStockButton.addActionListener(e -> {
            viewStockPanel.removeAll();
            viewStockPanel.add(viewStocks());
            cardLayout.show(getContentPane(), "ViewStock");
        });
        viewButtonPanel.add(viewStockButton);
        menuPanel.add(viewButtonPanel);

        // Create and style exit button
        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setLayout(flowLayout);
        MenuButtons exitButton = new MenuButtons("Exit", new Color(70, 130, 180), 20);
        exitButton.setFont(buttonFont);
        exitButtonPanel.setOpaque(false);
        exitButton.setForeground(fontColor);
        exitButton.addActionListener(e -> System.exit(0));
        exitButtonPanel.add(exitButton);
        menuPanel.add(exitButtonPanel);

        // Add glue component to push buttons to the bottom
        menuPanel.add(Box.createVerticalGlue());

        return menuPanel;
    }

    //ticker panel that prompts the user to enter a ticker symbol to then either go to modify or investigate
    private JPanel createTickerStockPanel() {
        // Set up panel with a background image
        BackgroundPanel tickerPanel = new BackgroundPanel(backgroundImage);
        tickerPanel.setLayout(new BoxLayout(tickerPanel, BoxLayout.Y_AXIS)); // Set layout as a box
        tickerPanel.setBorder(emptyBorder);

        tickerPanel.add(Box.createVerticalGlue());

        // Set up title to prompt user to input ticker symbol
        JLabel tickerPromptLabel = new JLabel("Enter Ticker Symbol", JLabel.CENTER);
        tickerPromptLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align label horizontally at the center
        tickerPromptLabel.setForeground(Color.BLACK); // Set font colour
        Font font = tickerPromptLabel.getFont();
        tickerPromptLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD)); // Set font and make font bold
        tickerPanel.add(tickerPromptLabel); // Add label to ticker panel

        // Add panel to insert ticker symbol
        JPanel tickerInputPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        tickerInputPanel.setOpaque(false); // Set background to be transparent
        tickerInputPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align panel horizontally at the center
        tickerInputPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        // Set up tickerfield for value of ticker
        JTextField tickerField = new JTextField();
        tickerField.setOpaque(false); // Set background of field to be transparent
        tickerInputPanel.add(new JLabel("", JLabel.CENTER)); // Add space about insert area
        tickerInputPanel.add(tickerField); // Add ticker value
        tickerPanel.add(tickerInputPanel); // Add to ticker panel

        // Add enter button
        EnterButtons enterButton = new EnterButtons("Enter", Color.WHITE, 10);
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align button horizontally at the center
        enterButton.setFont(buttonFont); // Set font
        enterButton.setForeground(fontColor); // Set font colour
        tickerPanel.add(enterButton); // Add to panel

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StockData stock = portfolio.getStckByTic(tickerField.getText());
                if(stock == null){
                    investigateStockPanel = createInvestigateStockPanel(tickerField.getText());
                }else{
                    investigateStockPanel = createInvestigateStockPanel(stock.getTic(),stock.getCP(),stock.getPr5DaA(),stock.getPr1MoA(), stock.getPr1YrA(), stock.getPer(),stock.getBta(), stock.getDiYi());

                }
                getContentPane().add(investigateStockPanel, "InvestigateStock"); // Update the instance variable
                cardLayout.show(getContentPane(), "InvestigateStock");
            }
        });
        tickerPanel.add(Box.createVerticalGlue());

        return tickerPanel; // Return the background panel with components
    }


    private JPanel createInvestigateStockPanel(String ticker) {
        webScraper scraper = new webScraper();
        StockData stock = scraper.scrape(ticker);

        //Container mainContainer = null;
        BackgroundPanel investigatePanel = new BackgroundPanel(backgroundImage);
        investigatePanel.setLayout(new BorderLayout(10, 10));
        investigatePanel.setBorder(emptyBorder);
        // Input panel with GridLayout
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        inputPanel.setOpaque(false);
        // Initialize the input fields
        tickerField = new JTextField(ticker);
        currentPriceField = new JTextField(String.valueOf(stock.getCP()));
        price5DaysAgoField = new JTextField(String.valueOf(stock.getPr5DaA()));
        price1MonthAgoField = new JTextField(String.valueOf(stock.getPr1MoA()));
        price1YearAgoField = new JTextField(String.valueOf(stock.getPr1YrA()));
        peRatioField = new JTextField(String.valueOf(stock.getPer()));
        betaField = new JTextField(String.valueOf(stock.getBta()));
        dividendYieldField = new JTextField(String.valueOf(stock.getDiYi()));

        // Add components to the input panel
        // Create and set properties for "Ticker Symbol" label
        JLabel ticLab = new JLabel("Ticker Symbol:");
        ticLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(ticLab);
        // Add tickerField to the inputPanel
        inputPanel.add(tickerField);
        // Create and set properties for "Current Price" label
        JLabel cPLab = new JLabel("Current Price:", JLabel.RIGHT);
        cPLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(cPLab);
        // Add currentPriceField to the inputPanel
        inputPanel.add(currentPriceField);
        // Create and set properties for "Price 5 Days Ago" label
        JLabel p5DALab = new JLabel("Price 5 Days Ago:", JLabel.RIGHT);
        p5DALab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(p5DALab);
        // Add price5DaysAgoField to the inputPanel
        inputPanel.add(price5DaysAgoField);
        // Create and set properties for "Price 1 Month Ago" label
        JLabel p1MALab = new JLabel("Price 1 Month Ago:", JLabel.RIGHT);
        p1MALab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(p1MALab);
        // Add price1MonthAgoField to the inputPanel
        inputPanel.add(price1MonthAgoField);
        // Create and set properties for "Price 1 Year Ago" label
        JLabel p1YALab = new JLabel("Price 1 Year Ago:", JLabel.RIGHT);
        p1YALab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(p1YALab);
        // Add price1YearAgoField to the inputPanel
        inputPanel.add(price1YearAgoField);
        // Create and set properties for "P/E Ratio" label
        JLabel peRLab = new JLabel("P/E Ratio:", JLabel.RIGHT);
        peRLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(peRLab);
        // Add peRatioField to the inputPanel
        inputPanel.add(peRatioField);
        // Create and set properties for "Beta" label
        JLabel beLab = new JLabel("Beta:", JLabel.RIGHT);
        beLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(beLab);
        // Add betaField to the inputPanel
        inputPanel.add(betaField);
        // Create and set properties for "Dividend Yield (%)" label
        JLabel dYLab = new JLabel("Dividend Yield (%):", JLabel.RIGHT);
        dYLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(dYLab);
        // Add dividendYieldField to the inputPanel
        inputPanel.add(dividendYieldField);

        for (Component component : inputPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(Color.black);
                label.setFont(new Font("Arial", Font.BOLD, 12));
            }
        }


        // Button panel with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); //set background to transparent
        // calculate button
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setPreferredSize(new Dimension(80, 30));//set size
        buttonPanel.add(calculateButton);//add to panel
        //update from internet button
        JButton updateInternetButton = new JButton("Update from Internet");
        updateInternetButton.setPreferredSize(new Dimension(120, 30)); //set size
        buttonPanel.add(updateInternetButton); //add to panel
        //update button
        JButton updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(80, 30)); //set size
        buttonPanel.add(updateButton); //add to panel
        //sell button
        JButton removeButton = new JButton("Sell");
        removeButton.setSize(new Dimension(80, 30)); //set size
        buttonPanel.add(removeButton); //add to panel
        //menu button
        JButton menuButton = new JButton("Menu");
        menuButton.setSize(new Dimension(80, 30)); //set size
        buttonPanel.add(menuButton); //add to panel

        // Decision label
        decisionLabel = new JLabel("The bot recommends you to: ", JLabel.CENTER);
        decisionLabel.setForeground(Color.black);
        Font font = decisionLabel.getFont();
        decisionLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));

        // Add components to the main panel

        investigatePanel.add(inputPanel, BorderLayout.CENTER);
        investigatePanel.add(buttonPanel, BorderLayout.SOUTH);
        investigatePanel.add(decisionLabel, BorderLayout.NORTH);

        // Add main panel to the frame
        add(investigatePanel);

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(getContentPane(), "Menu"); // Change mainContainer to getContentPane()
            }
        });
        // Action listener for the calculate button
        calculate1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateDecision();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStock();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStock();
            }
        });
        return investigatePanel;
    }

    private JPanel createInvestigateStockPanel(String ticker, double currentPrice, double price5daysAgo, double price1MonthAgo, double price1YearAgo, double peRatio, double beta, double dividend) {
        //Container mainContainer = null;
        BackgroundPanel investigatePanel = new BackgroundPanel(backgroundImage);
        investigatePanel.setLayout(new BorderLayout(10, 10));
        investigatePanel.setBorder(emptyBorder);
        // Input panel with GridLayout
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        inputPanel.setOpaque(false);
        // Initialize the input fields
        tickerField = new JTextField(ticker);
        currentPriceField = new JTextField(String.valueOf(currentPrice));
        price5DaysAgoField = new JTextField(String.valueOf(price5daysAgo));
        price1MonthAgoField = new JTextField(String.valueOf(price1MonthAgo));
        price1YearAgoField = new JTextField(String.valueOf(price1YearAgo));
        peRatioField = new JTextField(String.valueOf(peRatio));
        betaField = new JTextField(String.valueOf(beta));
        dividendYieldField = new JTextField(String.valueOf(dividend));

        // Add components to the input panel
        // Create and set properties for "Ticker Symbol" label
        JLabel ticLab = new JLabel("Ticker Symbol:");
        ticLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(ticLab);
        // Add tickerField to the inputPanel
        inputPanel.add(tickerField);
        // Create and set properties for "Current Price" label
        JLabel cPLab = new JLabel("Current Price:", JLabel.RIGHT);
        cPLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(cPLab);
        // Add currentPriceField to the inputPanel
        inputPanel.add(currentPriceField);
        // Create and set properties for "Price 5 Days Ago" label
        JLabel p5DALab = new JLabel("Price 5 Days Ago:", JLabel.RIGHT);
        p5DALab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(p5DALab);
        // Add price5DaysAgoField to the inputPanel
        inputPanel.add(price5DaysAgoField);
        // Create and set properties for "Price 1 Month Ago" label
        JLabel p1MALab = new JLabel("Price 1 Month Ago:", JLabel.RIGHT);
        p1MALab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(p1MALab);
        // Add price1MonthAgoField to the inputPanel
        inputPanel.add(price1MonthAgoField);
        // Create and set properties for "Price 1 Year Ago" label
        JLabel p1YALab = new JLabel("Price 1 Year Ago:", JLabel.RIGHT);
        p1YALab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(p1YALab);
        // Add price1YearAgoField to the inputPanel
        inputPanel.add(price1YearAgoField);
        // Create and set properties for "P/E Ratio" label
        JLabel peRLab = new JLabel("P/E Ratio:", JLabel.RIGHT);
        peRLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(peRLab);
        // Add peRatioField to the inputPanel
        inputPanel.add(peRatioField);
        // Create and set properties for "Beta" label
        JLabel beLab = new JLabel("Beta:", JLabel.RIGHT);
        beLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(beLab);
        // Add betaField to the inputPanel
        inputPanel.add(betaField);
        // Create and set properties for "Dividend Yield (%)" label
        JLabel dYLab = new JLabel("Dividend Yield (%):", JLabel.RIGHT);
        dYLab.setHorizontalAlignment(JLabel.RIGHT);
        inputPanel.add(dYLab);
        // Add dividendYieldField to the inputPanel
        inputPanel.add(dividendYieldField);

        for (Component component : inputPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(Color.black);
                Font font = label.getFont();
                label.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
            }
        }
        // Button panel with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.setOpaque(false);
        buttonPanel.add(calculateButton);

        JButton updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(updateButton);

        JButton removeButton = new JButton("Sell");
        removeButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(removeButton);

        JButton menuButton = new JButton("Menu");
        menuButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(menuButton);

        // Decision label
        decisionLabel = new JLabel("The bot recommends you to: ", JLabel.CENTER);
        decisionLabel.setForeground(Color.black);
        Font font = decisionLabel.getFont();
        decisionLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));

        // Add components to the main panel
        investigatePanel.add(inputPanel, BorderLayout.CENTER);
        investigatePanel.add(buttonPanel, BorderLayout.SOUTH);
        investigatePanel.add(decisionLabel, BorderLayout.NORTH);

        // Add main panel to the frame
        add(investigatePanel);

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(getContentPane(), "Menu"); // Change mainContainer to getContentPane()
            }
        });
        // Action listener for the calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateDecision();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStock();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStock();
            }
        });
        return investigatePanel;
    }

    private JPanel viewStocks() {
        JPanel viewStocksPanel = new JPanel(new BorderLayout());
        viewStocksPanel.setLayout(new BorderLayout(10, 10));
        viewStocksPanel.setBorder(emptyBorder);

        // Scrollable panel for stocks
        JPanel stocksPanel = new JPanel();
        stocksPanel.setLayout(new BoxLayout(stocksPanel, BoxLayout.Y_AXIS));
        stocksPanel.setOpaque(false);

        // Add stocks to the panel
        for (StockData stock : portfolio.getPortfolio()) {
            JPanel stockPanel = createStockPanel(stock);
            stocksPanel.add(stockPanel);
        }

        // Create a scroll pane
        JScrollPane scrollPane = new JScrollPane(stocksPanel);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        viewStocksPanel.add(scrollPane, BorderLayout.CENTER);

        // Menu button to go back
        JButton menuButton = new JButton("Menu");
        menuButton.addActionListener(e -> cardLayout.show(getContentPane(), "Menu"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(menuButton);
        buttonPanel.setOpaque(false);
        viewStocksPanel.add(buttonPanel, BorderLayout.SOUTH);

        return viewStocksPanel;
    }

    private JPanel createStockPanel(StockData stockD) {

        JPanel stockPanel = new JPanel(new BorderLayout());
        stockPanel.setBorder(emptyBorder);
        stockPanel.setOpaque(false);

        // Create labels for each attribute
        JLabel ticLab = new JLabel("Ticker: " + stockD.getTic(),SwingConstants.CENTER);
        JLabel cPLab = new JLabel("Current Price: " + stockD.getCP(),SwingConstants.CENTER);
        JLabel p5DALab = new JLabel("Price 5 Days Ago: " + stockD.getPr5DaA(),SwingConstants.CENTER);
        JLabel p1MALab = new JLabel("Price 1 Month Ago: " + stockD.getPr1MoA(),SwingConstants.CENTER);
        JLabel p1YALab = new JLabel("Price 1 Year Ago: " + stockD.getPr1YrA(),SwingConstants.CENTER);
        JLabel beLab = new JLabel("Beta: " + stockD.getBta(),SwingConstants.CENTER);
        JLabel peRLab = new JLabel("P/E Ratio: " + stockD.getPer(),SwingConstants.CENTER);
        JLabel dYLab = new JLabel("Dividend Yield: " + stockD.getDiYi(),SwingConstants.CENTER);

        // Create a panel to hold the labels
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(ticLab);
        labelPanel.add(cPLab);
        labelPanel.add(p5DALab);
        labelPanel.add(p1MALab);
        labelPanel.add(p1YALab);
        labelPanel.add(beLab);
        labelPanel.add(peRLab);
        labelPanel.add(dYLab);

        // Add label panel to stock panel
        stockPanel.setOpaque(false);
        stockPanel.add(labelPanel, BorderLayout.CENTER);

        return stockPanel;
    }

    /*
    private JPanel createStockPanel(StockData stock) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(stock.getTicker()));
        panel.setPreferredSize(new Dimension(600, 50));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel stockInfo = new JLabel(stock.toString());
        infoPanel.add(stockInfo);
        panel.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setPreferredSize(new Dimension(100, 25));
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement calculate logic here
            }
        });
        buttonPanel.add(calculateButton);

        JButton updateButton = new JButton("Update from Internet");
        updateButton.setPreferredSize(new Dimension(140, 25));
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement update logic here
            }
        });
        buttonPanel.add(updateButton);

        JButton modifyButton = new JButton("Modify");
        modifyButton.setPreferredSize(new Dimension(100, 25));
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement modify logic here
            }
        });
        buttonPanel.add(modifyButton);

        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }
*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
    private void calculateDecision() {
        try {
            // Get the input values
            double cP = Double.parseDouble(currentPriceField.getText());
            double p5A = Double.parseDouble(price5DaysAgoField.getText());
            double p1MA = Double.parseDouble(price1MonthAgoField.getText());
            double p1YA = Double.parseDouble(price1YearAgoField.getText());
            double peR = Double.parseDouble(peRatioField.getText());
            double beta = Double.parseDouble(betaField.getText());
            double dY = Double.parseDouble(dividendYieldField.getText());

            // Calculate SMA and RSI
            double sma = calculateSMA(p5A, p1MA, p1YA);
            double rsi = calculateRSI(p5A, p1MA, p1YA, cP);

            // Make a decision based on the calculated values and input metrics
            String decision = makeDecision(cP, sma, rsi, peR, beta, dY);

            // Display the decision
            System.out.println("The bot recommends you to: " + decision);
            decisionLabel.setText("The bot recommends you to: " + decision);


        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateStock() {
        try {
            // Get the input values
            String tic = tickerField.getText();
            double cP = Double.parseDouble(currentPriceField.getText());
            double p5A = Double.parseDouble(price5DaysAgoField.getText());
            double p1MA = Double.parseDouble(price1MonthAgoField.getText());
            double p1YA = Double.parseDouble(price1YearAgoField.getText());
            double peR = Double.parseDouble(peRatioField.getText());
            double beta = Double.parseDouble(betaField.getText());
            double dY = Double.parseDouble(dividendYieldField.getText());

            // Update the stock information in the portfolio
            portfolio.updtstck(tic, cP, p5A, p1MA, p1YA, peR, beta, dY);

            // Optionally, you can display a success message
            JOptionPane.showMessageDialog(null, "Stock information updated successfully.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addStock() {
        try {
            // Get the input values
            String tic = tickerField.getText();
            double cP = Double.parseDouble(currentPriceField.getText());
            double p5A = Double.parseDouble(price5DaysAgoField.getText());
            double p1MA = Double.parseDouble(price1MonthAgoField.getText());
            double p1YA = Double.parseDouble(price1YearAgoField.getText());
            double peR = Double.parseDouble(peRatioField.getText());
            double beta = Double.parseDouble(betaField.getText());
            double dY = Double.parseDouble(dividendYieldField.getText());

            portfolio.addStck(new StockData(tic, cP, p5A, p1MA, p1YA, peR, beta, dY));
            JOptionPane.showMessageDialog(null, "Stock added to portfolio.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void removeStock() {
        String ticker = tickerField.getText();
        portfolio.removeStock(ticker);
    }
    private double calculateSMA(double pr5DaA, double pr1MoA, double pr1YrA) {
        return (pr5DaA + pr1MoA + pr1YrA) / 3;
    }

    private double calculateRSI(double pr5DaA, double pr1MoA, double pr1YrA, double cP) {
        double avgGain = 0;
        double avgLoss = 0;
        double[] priceChanges = {cP - pr5DaA, pr5DaA - pr1MoA, pr1MoA - pr1YrA};

        for (double change : priceChanges) {
            if (change > 0) {
                avgGain += change;
            } else {
                avgLoss -= change;
            }
        }

        avgGain /= priceChanges.length;
        avgLoss /= priceChanges.length;

        if (avgLoss == 0) {
            return 100;
        }

        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }
    private String makeDecision(double cP, double sma, double rsi, double per, double bta, double diYi) {
        // Sample decision logic combining various metrics
        if (cP > sma && rsi < 70 && per < 20 && bta < 1.5 && diYi > 2) {
            return "Buy";
        } else if (cP < sma && rsi > 30 && per > 25 && bta > 1.5 && diYi < 2) {
            return "Sell";
        } else {
            return "Hold";
        }
    }
}
