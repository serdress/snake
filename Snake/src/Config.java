/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alu23847452v
 */
public class Config {

    private static Config instance = null;
    public static int numRows = 30;
    public static int numCols = 40;

    public int score = 0;

    private Config() {

    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
