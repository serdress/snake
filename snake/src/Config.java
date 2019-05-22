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

    private Config() {
    }

    public static Config getIstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public int numRows = 30;
    public int numCols = 40;


}
