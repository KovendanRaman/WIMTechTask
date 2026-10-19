import java.util.*;

public class FrequencyAllocator{

    private static final int [] frequencies = {110, 111, 112, 113, 114, 115};
    private static final double threshold = 300.0;

    static class Cell{
        String id;
        double easting;
        double northing;
        int frequency = -1;
        List<Cell> neighbours = new ArrayList<>();
        Cell(String id, double easting, double northing){
            this.id = id;
            this.easting =  easting;
            this.northing = northing;
        }

    }
    private static List<Cell> loadCellData() {
        return Arrays.asList(
            new Cell("A", 536660, 183800), new Cell("B", 537032, 184006),
            new Cell("C", 537109, 183884), new Cell("D", 537110, 184695),
            new Cell("E", 537206, 184685), new Cell("F", 537248, 185016),
            new Cell("G", 537250, 185020), new Cell("H", 537267, 184783),
            new Cell("I", 537269, 183451), new Cell("J", 537270, 184140),
            new Cell("K", 537356, 184927), new Cell("L", 537380, 184727),
            new Cell("M", 537458, 184495), new Cell("N", 537604, 184134),
            new Cell("O", 537720, 184057), new Cell("P", 537905, 184591),
            new Cell("Q", 537910, 184441), new Cell("R", 537953, 184295),
            new Cell("S", 538050, 184245)
        );
    }

    private static void inferenceGraph(List<Cell> cells){
        for (int i = 0; i < cells.size(); i++) {
            for (int j = i+1; j < cells.size(); j++) {
                Cell c1 = cells.get(i);
                Cell c2 = cells.get(j);

                double dist = Math.hypot(c1.easting - c2.easting, c1.northing - c2.northing);
                if(dist< threshold){
                    c1.neighbours.add(c2);
                    c2.neighbours.add(c1);
                }
            }
        }
    }

    private static void matchFrequencies(List<Cell> cells){
        List<Cell> sortedCells = new ArrayList<>(cells);
        sortedCells.sort((c1, c2) -> Integer.compare(c2.neighbours.size(), c1.neighbours.size()));
        for (Cell cell : sortedCells) {
            Set<Integer> usedFrequencies = new HashSet<>();
            for (Cell neighbour : cell.neighbours) {
                if (neighbour.frequency != -1) {
                    usedFrequencies.add(neighbour.frequency);
                }
            }
            for (int frequency : frequencies) {
                if(!usedFrequencies.contains(frequency)){
                    cell.frequency = frequency;
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        List<Cell> network = loadCellData();
        inferenceGraph(network);
        matchFrequencies(network);


        network.sort(Comparator.comparing(c -> c.id));
        for (Cell c : network) {
        System.out.println("Cell ID: " + c.id + " -> Frequency: " + c.frequency);
    }
    }
    
}