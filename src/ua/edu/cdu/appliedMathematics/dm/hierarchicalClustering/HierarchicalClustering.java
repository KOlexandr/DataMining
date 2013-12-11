package ua.edu.cdu.appliedMathematics.dm.hierarchicalClustering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HierarchicalClustering {

    private int minPt;
    private double eps;
    private List<Point> d;
    private Cluster noiseCluster;
    private List<Cluster> resultClusters;
    private List<List<Cluster>> allClusters;
    private InterClassSimilarityType similarityType;

    /**
     * initialise and classify set of given points
     * @param d - set of initial points
     * @param eps - similarity of clusters, if similarity less then eps we can join this clusters
     * @param minPt - minimum number of points for cluster? if cluster has less number of points this cluster is noise
     * @param similarityType - type of counting similarity between clusters
     */
    public HierarchicalClustering(List<Point> d, double eps, int minPt, InterClassSimilarityType similarityType) {
        this.d = d;
        this.eps = eps;
        this.minPt = minPt;
        this.allClusters = new ArrayList<>();
        this.similarityType = similarityType;
        classify();
    }

    /**
     * classify given data set
     */
    private void classify(){
        int step = 0;
        List<Cluster> oneStepClusters = new ArrayList<>();
        List<Cluster> prevStepClusters = null;
        //create clusters with only one point
        for (Point point : d) {
            Cluster cluster = new Cluster(step);
            cluster.getPoints().add(point);
            oneStepClusters.add(cluster);
        }
        //add list of clusters to list of lists of clusters as clusters of 0 step
        allClusters.add(oneStepClusters);
        //create similarity matrix
        double[][] s = new double[oneStepClusters.size()][oneStepClusters.size()];
        //fill similarity matrix
        for (int i = 0; i < oneStepClusters.size(); i++) {
            for (int j = 0; j < oneStepClusters.size(); j++) {
                s[i][j] = sim(oneStepClusters.get(i), oneStepClusters.get(j));
            }
        }
        step++;

        //while previous cluster not equals new cluster
        while(!oneStepClusters.equals(prevStepClusters)){
            prevStepClusters = oneStepClusters;
            oneStepClusters = new ArrayList<>();
            //iterate on all clusters
            for (int i = 0; i < prevStepClusters.size(); i++) {
                final Cluster c = prevStepClusters.get(i);
                //if not processes yet => process
                if(!c.isProcessed()){
                    c.setProcessed(true);
                    //find closest cluster
                    Cluster closest = findClosestCluster(prevStepClusters, s[i]);
                    //if found
                    if(null != closest){
                        //process closest cluster
                        closest.setProcessed(true);
                        //create new cluster of current step
                        Cluster cluster = new Cluster(step);
                        cluster.getPoints().addAll(c.getPoints());
                        cluster.getPoints().addAll(closest.getPoints());
                        //join points from 2 clusters in new cluster
                        //and add this cluster to list of clusters for current step
                        oneStepClusters.add(cluster);
                    } else {
                        //if not found closest add this cluster to list of clusters for current step
                        oneStepClusters.add(c);
                    }
                }
            }
            //add list of all clusters for current step to list of list
            allClusters.add(oneStepClusters);
            //create and fill new similarity matrix for clusters of current step
            s = new double[oneStepClusters.size()][oneStepClusters.size()];
            for (int i = 0; i < oneStepClusters.size(); i++) {
                for (int j = 0; j < oneStepClusters.size(); j++) {
                    s[i][j] = sim(oneStepClusters.get(i), oneStepClusters.get(j));
                }
                s[i][i] = 0;
            }
            //set all clusters as not processed
            for (Cluster cluster : oneStepClusters) {
                cluster.setProcessed(false);
            }
            step++;
        }
        //clusters on 2 last steps equals that mean we classified all point
        //lets process results (find noise and create for noise points one specific cluster)
        //and create list with clusters (without noise)
        processResults();
    }

    /**
     * process list of clusters that program get on last step
     * if number of points in any cluster less then minPt points from this cluster is noise
     */
    private void processResults() {
        noiseCluster = new Cluster();
        resultClusters = new ArrayList<>();
        for (Cluster cluster : allClusters.get(allClusters.size()-1)) {
            if(cluster.getPoints().size() < minPt){
                noiseCluster.getPoints().addAll(cluster.getPoints());
            } else {
                resultClusters.add(cluster);
            }
        }
    }

    /**
     * find cluster that more similar to this cluster
     * @param clusters - list of all clusters on current step
     * @param s - line of similarity matrix for this cluster
     * @return closest cluster or null if no any closest cluster
     */
    private Cluster findClosestCluster(final List<Cluster> clusters, final double[] s) {
        int idx = -1;
        double dist = Double.MAX_VALUE;
        //finding cluster: distance from this cluster to closest less then eps and closest cluster is not processed yet
        //and closest not equals this cluster
        for (int i = 0; i < s.length; i++) {
            if(s[i] != 0 && dist > s[i] &&
                    s[i] < eps && !clusters.get(i).isProcessed()){
                dist = s[i];
                idx = i;
            }
        }
        if(idx != -1){
            s[idx] = 0;
            return clusters.get(idx);
        } else {
            return null;
        }
    }

    /**
     * Define Inter-Cluster Similarity
     * 1. use min distance between 2 points from 2 clusters
     * 2. use max distance between 2 points from 2 clusters
     * 3. use all distances between each points of 2 clusters
     * @param ci - first cluster
     * @param cj - second cluster
     * @return similarity of clusters (distance between clusters)
     */
    private double sim(final Cluster ci, final Cluster cj){
        double sim = 0;
        switch (similarityType){
            case MAX:
                double maxDistance = 0;
                for (Point dk : ci.getPoints()) {
                    for (Point dp : cj.getPoints()) {
                        if(!dk.equals(dp) && maxDistance < dk.distance(dp)){
                            maxDistance = dk.distance(dp);
                        }
                    }
                }
                sim = maxDistance;
                break;
            case MIN:
                double minDistance = Double.MAX_VALUE;
                for (Point dk : ci.getPoints()) {
                    for (Point dp : cj.getPoints()) {
                        if(!dk.equals(dp) && minDistance > dk.distance(dp)){
                            minDistance = dk.distance(dp);
                        }
                    }
                }
                sim = minDistance;
                break;
            case GROUP_AVERAGE:
                double sumDistances = 0;
                final Set<Point> newCluster = new HashSet<>();
                newCluster.addAll(ci.getPoints());
                newCluster.addAll(cj.getPoints());
                for (Point dk : newCluster) {
                    for (Point dp : newCluster) {
                        if(!dk.equals(dp)){
                            sumDistances += dk.distance(dp);
                        }
                    }
                }
                sim = sumDistances/((ci.getPoints().size() + cj.getPoints().size())*((ci.getPoints().size() + cj.getPoints().size() - 1)));
                break;
        }

        return sim;
    }

    public Cluster getNoiseCluster() {
        return noiseCluster;
    }

    public List<Cluster> getResultClusters() {
        return resultClusters;
    }
}