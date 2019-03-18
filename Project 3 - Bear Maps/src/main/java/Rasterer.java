import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */

public class Rasterer {

    private double lrlon;
    private double ullon;
    private double w;
    private double h;
    private double ullat;
    private double lrlat;
    private double queryLonDPP;

    private static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    private static final int TILE_SIZE = 256;
    private static final double ROOT_LONDPP = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;

    public Rasterer() {
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);

        this.lrlon = params.get("lrlon");
        this.ullon = params.get("ullon");
        this.w = params.get("w");
        this.h = params.get("h");
        this.ullat = params.get("ullat");
        this.lrlat = params.get("lrlat");
        this.queryLonDPP = getLonDPP(lrlon, ullon, w);

        int depth = getDepth(queryLonDPP);

        int[] xPos = fileStartEndX(ullon,lrlon,depth);
        int[] yPos = fileStartEndY(ullat,lrlat,depth);

        int row = 0;
        int col = 0;

        int rowLen = (yPos[1] - yPos[0]) + 1;
        int colLen = (xPos[1] - xPos[0]) + 1;

        String[][] fileNames = new String[rowLen][colLen];

        for(int y = yPos[0];y < yPos[1];y++){
            for(int x = xPos[0];x < xPos[1];x++){
                fileNames[row][col] = convertToFileName(depth,x,y);
                col++;
            }
            col = 0;
            row++;
        }
        Map<String, Object> results = new HashMap<>();

        results.put("render_grid",fileNames);

        return results;
    }

    /**
     * Calculates the longitudinal distance per pixel (LonDPP) value.
     */
    private double getLonDPP(double lrlon, double ullon, double w){
        return (lrlon - ullon) / w;
    }

    /**
     * Returns the best depth, given a LonDPP value.
     * If the LonDPP requested is less than what is available in the files, return the maximum depth (7).
     */
    private int getDepth(double LonDPP){
        for(int i = 0;i < 8;i++){
            if(ROOT_LONDPP / Math.pow(2,i) <= LonDPP){
                return i;
            }
        }
        return 7;
    }

    /**
     * Returns the longitudinal distance per tile at a given depth.
     */
    private double getLonDistancePerTile(int d){
        return (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2,d));
    }

    /**
     * Returns the latitudinal distance per tile at a given depth.
     */
    private double getLatDistancePerTile(int d){
        return (ROOT_ULLAT - ROOT_LRLAT) / (Math.pow(2,d));
    }

    /**
     * Returns an int array that stores the x coordinates of the first and last images
     * that intersect the query box.
     */
    private int[] fileStartEndX(double queryUllon, double queryLrlon, int depth){

        int startX = (int) ((queryUllon - ROOT_ULLON) / getLonDistancePerTile(depth));
        int endX = (int) ((queryLrlon - ROOT_ULLON) / getLonDistancePerTile(depth));
        return new int[] {startX,endX};
    }

    /**
     * Returns an int array that stores the y coordinates of the first and last images
     * that intersect the query box.
     */
    private int[] fileStartEndY(double queryUllat, double queryLrlat, int depth){

        int startX = (int) ((ROOT_ULLAT - queryUllat) / getLatDistancePerTile(depth));
        int endX = (int) ((ROOT_ULLAT - queryLrlat) / getLatDistancePerTile(depth));
        return new int[] {startX,endX};
    }

    private String convertToFileName(int d, int x, int y){
        return "d" + d + "_x" + x + "_y" + y + ".png";
    }
}
