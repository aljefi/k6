import java.util.*;

/**
 * Container class to different classes, that makes the whole
 * set of classes one class formally.
 */
public class GraphTask {

    public boolean backwards = true;
    public double km;
    public double dollar;
    int[][] matrix;
    Vertex start;
    Vertex finish;
    HashMap<Vertex, Double> priceToAllPoints = new HashMap<>();
    HashMap<Vertex, Double> distToAllPoints = new HashMap<>();
    HashMap<Vertex, Boolean> alreadyWas = new HashMap<>();


    /**
     * Main method.
     */
    public static void main(String[] args) {
        GraphTask a = new GraphTask();
        a.run();
    }

    /**
     * Actual main method to run examples and everything.
     */
    public void run() {
        Graph g = new Graph("G");
        g.createRandomSimpleGraph(4, 3);
        System.out.println(g);
        // type here where From and where to we are going
        Vertex vFrom = new Vertex("v4", null, null);
        Vertex vTo = new Vertex("v1", null, null);
        System.out.println("---");
        List<List<Arc>> minRoutes = g.findRoute(vFrom, vTo);

        System.out.println(distToAllPoints.get(finish));
        System.out.println("Route with cheapest price("+priceToAllPoints.get(finish)+"$)=" + minRoutes.get(0));
        System.out.println("Route with minimum distance("+distToAllPoints.get(finish)+"km)=" + minRoutes.get(1));
        System.out.println("Route with minimum points=" + minRoutes.get(2));

//      Graph h = new Graph ("H");
//      h.createRandomSimpleGraph (6, 9);
//      System.out.println (h);

        // TODO!!! Your experiments here
    }

    /**
     * Creating Vertex
     */
    // TODO!!! add javadoc relevant to your problem
    class Vertex {

        private final String id;
        private Vertex next;
        private Arc first;
        private int info = 0;
        // You can add more fields, if needed

        /**
         * @param s id of Vertex
         * @param v next Vertex
         * @param e Arc of Vertex
         */
        Vertex(String s, Vertex v, Arc e) {
            id = s;
            next = v;
            first = e;
        }

        Vertex(String s) {
            this(s, null, null);
        }

        @Override
        public String toString() {
            return id;
        }

        // TODO!!! Your Vertex methods here!
    }


    /**
     * Arc represents one arrow in the graph. Two-directional edges are
     * represented by two Arc objects (for both directions).
     */
    class Arc {

        private final String id;
        private Vertex target;
        private Arc next;
        // You can add more fields, if needed
        private final double distance;
        private final double price;

        /**
         * Creates Arc with fixed distance and price
         *
         * @param s Arc id
         * @param v Arc target
         * @param a next Arc
         */
        Arc(String s, Vertex v, Arc a) {
            id = s;
            target = v;
            next = a;

            // as math.random can be 0-1, we add +1 (distance can't be 0)
            // and
            // multiply by *100(distance between station can't be 1-2 km)
            if (backwards) {
                km = Math.round((Math.random() / Math.random() / Math.random() + 1) * 100);
                dollar = Math.round((Math.random() / Math.random() / Math.random() + 1) * 100);
                backwards = false;
            } else backwards = true;
            distance = km;
            price = dollar;
            System.out.println(id + ": " + distance + "km" + "|" + price + "$");

        }

        /**
         * Initializes empty Arc with id only
         * @param s Arc id
         */
        Arc(String s) {
            this(s, null, null);
        }

        @Override
        public String toString() {
            return id;
        }

        // TODO!!! Your Arc methods here!
    }


    class Graph {

        private final String id;
        private Vertex first;
        // You can add more fields, if needed

        /**
         * @param s Graph id
         * @param v Vertex id
         */
        Graph(String s, Vertex v) {
            id = s;
            first = v;
        }

        /**
         * @param s Initializes empty Graph with id only
         */
        Graph(String s) {
            this(s, null);
        }

        @Override
        public String toString() {
            String nl = System.getProperty("line.separator");
            StringBuilder sb = new StringBuilder(nl);
            sb.append(id);
            sb.append(nl);
            Vertex v = first;
            while (v != null) {
                sb.append(v);
                sb.append(" -->");
                Arc a = v.first;
                while (a != null) {
                    sb.append(" ");
                    sb.append(a);
                    sb.append(" (");
                    sb.append(v);
                    sb.append("->");
                    sb.append(a.target.toString());
                    sb.append(")");
                    a = a.next;
                }
                sb.append(nl);
                v = v.next;
            }
            return sb.toString();
        }

        /**
         * @param vid Vertex id
         * @return Vertex
         */
        public Vertex createVertex(String vid) {
            Vertex res = new Vertex(vid);
            res.next = first;
            first = res;
            return res;
        }

        /**
         * @param aid creating New Arc
         * @param from assigning from which Vertex
         * @param to assigning to which Vertex
         */
        public void createArc(String aid, Vertex from, Vertex to) {
            Arc res = new Arc(aid);
            res.next = from.first;
            from.first = res;
            res.target = to;
        }

        /**
         * Create a connected undirected random tree with n vertices.
         * Each new vertex is connected to some random existing vertex.
         *
         * @param n number of vertices added to this graph
         */
        public void createRandomTree(int n) {
            if (n <= 0)
                return;
            Vertex[] varray = new Vertex[n];
            for (int i = 0; i < n; i++) {
                varray[i] = createVertex("v" + (n - i));
                if (i > 0) {
                    int vnr = (int) (Math.random() * i);
                    createArc("a" + varray[vnr].toString() + "_"
                            + varray[i].toString(), varray[vnr], varray[i]);
                    createArc("a" + varray[i].toString() + "_"
                            + varray[vnr].toString(), varray[i], varray[vnr]);
                }
            }
        }

        /**
         * Create an adjacency matrix of this graph.
         * Side effect: corrupts info fields in the graph
         *
         * @return adjacency matrix
         */
        public int[][] createAdjMatrix() {
            int info = 0;
            Vertex v = first;
            while (v != null) {
                v.info = info++;
                v = v.next;
            }
            int[][] res = new int[info][info];
            v = first;
            while (v != null) {
                int i = v.info;
                Arc a = v.first;
                while (a != null) {
                    int j = a.target.info;
                    res[i][j]++;
                    a = a.next;
                }
                v = v.next;
            }
            matrix = res;
            return res;
        }

        /**
         * Create a connected simple (undirected, no loops, no multiple
         * arcs) random graph with n vertices and m edges.
         *
         * @param n number of vertices
         * @param m number of edges
         */
        public void createRandomSimpleGraph(int n, int m) {
            if (n <= 0)
                return;
            if (n > 2500)
                throw new IllegalArgumentException("Too many vertices: " + n);
            if (m < n - 1 || m > n * (n - 1) / 2)
                throw new IllegalArgumentException
                        ("Impossible number of edges: " + m);
            first = null;
            createRandomTree(n);       // n-1 edges created here
            Vertex[] vert = new Vertex[n];
            Vertex v = first;
            int c = 0;
            while (v != null) {
                vert[c++] = v;
                v = v.next;
            }
            int[][] connected = createAdjMatrix();
            int edgeCount = m - n + 1;  // remaining edges
            while (edgeCount > 0) {
                int i = (int) (Math.random() * n);  // random source
                int j = (int) (Math.random() * n);  // random target
                if (i == j)
                    continue;  // no loops
                if (connected[i][j] != 0 || connected[j][i] != 0)
                    continue;  // no multiple edges
                Vertex vi = vert[i];
                Vertex vj = vert[j];
                createArc("a" + vi.toString() + "_" + vj.toString(), vi, vj);
                connected[i][j] = 1;
                createArc("a" + vj + "_" + vi, vj, vi);
                connected[j][i] = 1;
                edgeCount--;  // a new edge happily created
            }
        }

        // TODO!!! Your Graph methods here! Probably your solution belongs here.

        /**
         * @param vFrom from which point
         * @param vTo to which point
         * @return list with cheapest, min destination and shortest(cheapest/minDest) routes
         */
        public List<List<Arc>> findRoute(Vertex vFrom, Vertex vTo) {
            System.out.println("From:   " + vFrom);
            System.out.println("To:     " + vTo);
            List<List<Arc>> ret = new ArrayList<>();
            Vertex tempS = this.first;
            Vertex tempF = this.first;
            String findStart = tempS.id;
            String findFinish = tempF.id;
            // finding start
            while (!findStart.equals(vFrom.id)) {
                tempS = tempS.next;
                findStart = tempS.toString();
            }
            start = tempS;
            findPrice(start);
            findDist(start);
            priceToAllPoints.put(start, 0.0);
            distToAllPoints.put(start, 0.0);
            System.out.println("price:" + priceToAllPoints);
            System.out.println("dist:" + distToAllPoints);
            // finding finish
            while (!findFinish.equals(vTo.id)) {
                tempF = tempF.next;
                findFinish = tempF.toString();
            }
            finish = tempF;
            Double routePrice = priceToAllPoints.get(finish);
            Double routeDist = distToAllPoints.get(finish);
            List<Arc> byPrice = findRoutePrice(finish, start, routePrice);
            List<Arc> byDist = findRouteDist(finish, start, routeDist);
            ret.add(byPrice);
            ret.add(byDist);
            List<Arc> leastStops;
            if (byPrice.size() > byDist.size()) {
                leastStops = byDist;
            } else {
                leastStops = byPrice;
            }
            ret.add(leastStops);
            return ret;
        }

        /**
         * @param finish finish Vertex
         * @param start start Vertex
         * @param route price of route
         * @return route, with the cheapest price
         */
        public List<Arc> findRoutePrice(Vertex finish, Vertex start, Double route) {
            Arc currentArc = finish.first;
//            StringBuilder ret = new StringBuilder();
            List<Arc> ret = new LinkedList<>();
//            ret.append(finish.id);

            while (!currentArc.target.equals(start)) {
                if (route - currentArc.price == priceToAllPoints.get(currentArc.target)) {
                    route = priceToAllPoints.get(currentArc.target);
//                    ret.append(" <- ");
                    ret.add(currentArc);
                    currentArc = currentArc.target.first;
                } else {
                    currentArc = currentArc.next;
                }
            }
//            ret.append(" <- ");
            ret.add(currentArc);
            return ret;
        }

        /**
         * @param finish finish Vertex
         * @param start start Vertex
         * @param route destination of route
         * @return route, with minimum destination
         */
        public List<Arc> findRouteDist(Vertex finish, Vertex start, Double route) {
            Arc currentArc = finish.first;
//            StringBuilder ret = new StringBuilder();
            List<Arc> ret = new LinkedList<>();
//            ret.append(finish.id);

            while (!currentArc.target.equals(start)) {
                if (route - currentArc.distance == distToAllPoints.get(currentArc.target)) {
                    route = distToAllPoints.get(currentArc.target);
//                    ret.append(" <- ");
                    ret.add(currentArc);
                    currentArc = currentArc.target.first;
                } else {
                    currentArc = currentArc.next;
                }
            }
//            ret.append(" <- ");
            ret.add(currentArc);
            return ret;
        }

        /**
         * Finds minimal prices to all points from start
         * @param start start Vertex
         */
        public void findPrice(Vertex start) {
            Vertex temp = this.first;
            while (temp != null) {
                if (temp == start) {
                    priceToAllPoints.put(temp, 0.0);
                    alreadyWas.put(temp, false);
                } else {

                    if (temp.first.next == null) {
                        alreadyWas.put(temp, true);
                    } else alreadyWas.put(temp, false);
                    priceToAllPoints.put(temp, Double.MAX_VALUE);
                }
                temp = temp.next;
            }
            while (alreadyWas.containsValue(false)) {
                if (start == null) {
                    break;
                }
                start = priceHelpRoute(start);
            }
        }

        /**
         * Finds minimal destination to all points from start
         * @param start start Vertex
         */
        public void findDist(Vertex start) {
            Vertex temp = this.first;
            while (temp != null) {
                if (temp == start) {
                    distToAllPoints.put(temp, 0.0);
                    alreadyWas.put(temp, false);
                } else {
                    if (temp.first.next == null) {
                        alreadyWas.put(temp, true);
                    } else alreadyWas.put(temp, false);
                    distToAllPoints.put(temp, Double.MAX_VALUE);
                }
                temp = temp.next;
            }
            while (alreadyWas.containsValue(false)) {
                if (start == null) {
                    break;
                }
                start = distHelpRoute(start);
            }
        }

        /**
         * Helping function to findPrice
         * @param start start Vertex
         * @return null, but uses recursion
         */
        public Vertex priceHelpRoute(Vertex start) {
            if (start == null) {
                return null;
            }
            alreadyWas.put(start, true);
            Arc startArcs = start.first;
            double priceToInterim = priceToAllPoints.get(start);
            while (startArcs != null) { // dist from start to point
                double priceNew = startArcs.price + priceToInterim;
                double pricePrev = priceToAllPoints.get(startArcs.target);
                if (priceNew < pricePrev) {
                    priceToAllPoints.put(startArcs.target, priceNew);
                }
                startArcs = startArcs.next;
            }
            Double tempVal = priceToAllPoints.get(start);
            priceToAllPoints.put(start, Double.MAX_VALUE);
            double minVal = 9999.00;
            for (Map.Entry<Vertex, Double> entry :
                    priceToAllPoints.entrySet()) {
                if (entry.getValue() < minVal && !alreadyWas.get(entry.getKey())) {
                    minVal = entry.getValue();
                }
            }
            Vertex newStart = null;
            for (Map.Entry<Vertex, Double> entry : priceToAllPoints.entrySet()) {
                if (entry.getValue().equals(minVal) && entry.getValue() != 0.0 && !alreadyWas.get(entry.getKey())) {
                    newStart = entry.getKey();
                    break;
                }
            }
            if (tempVal != 0.0) priceToAllPoints.put(start, tempVal);
            return priceHelpRoute(newStart);
        }

        /**
         * Helping function to findDist
         * @param start start Vertex
         * @return null, but uses recursion
         */
        public Vertex distHelpRoute(Vertex start) {
            if (start == null) {
                return null;
            }
            alreadyWas.put(start, true);
            Arc startArcs = start.first;
            double distToInterim = distToAllPoints.get(start);
            while (startArcs != null) { // dist from start to point
                double distNew = startArcs.distance + distToInterim;
                double distPrev = distToAllPoints.get(startArcs.target);
                if (distNew < distPrev) {
                    distToAllPoints.put(startArcs.target, distNew);
                }
                startArcs = startArcs.next;
            }
            Double tempVal = distToAllPoints.get(start);
            distToAllPoints.put(start, Double.MAX_VALUE);
            double minVal = 9999.00;
            for (Map.Entry<Vertex, Double> entry :
                    distToAllPoints.entrySet()) {
                if (entry.getValue() < minVal && !alreadyWas.get(entry.getKey())) {
                    minVal = entry.getValue();
                }
            }
            Vertex newStart = null;
            for (Map.Entry<Vertex, Double> entry : distToAllPoints.entrySet()) {
                if (entry.getValue().equals(minVal) && entry.getValue() != 0.0 && !alreadyWas.get(entry.getKey())) {
                    newStart = entry.getKey();
                    break;
                }
            }
            if (tempVal != 0.0) distToAllPoints.put(start, tempVal);
            return distHelpRoute(newStart);
        }
    }
} 
