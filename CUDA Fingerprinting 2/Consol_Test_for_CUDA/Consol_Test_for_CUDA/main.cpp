#define _USE_MATH_DEFINES 
#include <iostream>
#include "stdlib.h"
#include "stdio.h"								
#include <time.h>								// time allows to initialize random seed for the pseudo-random number generator algorithm
#include <iomanip>								// for printing n symbols after point
#include <math.h>								// for using const PI
using namespace std;

struct  Point{									// point on the plane
	int  x;										// coordinates of the point
	int  y;
	
	void printPoint(){							// print point coordinates by example (x, y)
		cout << "(" << x << ", " << y << ")";
	}
	void setPoint(int x, int y){				// set coordinates x and y to the point
		this->x = x;
		this->y = y;
		this->printPoint();
		cout << endl;
	}
	void turn_angle(double angle){				// turn Point by the angle
		double tx = this->x;
		double ty = this->y;
		this->x = (int)(tx * cos(angle) + ty * sin(angle));
		this->y = (int)(- tx * sin(angle) + ty * cos(angle));
	}
};

struct Dist{									// value keep dictance between point[i] and point[j]
	double value;
	int i;
	int j;
};

int compare_exact(const void* a, const void* b){			// compare-function by exact matching for quick sorting of the Dist-structure array
	double a_value = ((const struct Dist*)a)->value;		// conver void* to Dist* and select the field "value" for comparison
	double b_value = ((const struct Dist*)b)->value;

	if (a_value < b_value) return -1;						// comparison
	if (a_value > b_value) return 1;
	// if (a_value == b_value) 
	return 0;
}

int binarySearch(Dist* dist, int low, int hight, double key, const double DELTA){		// find the distance in ordered array dist which absolute value differs from the key less then on DELTA
	int index = (low + hight) / 2;
	// cout << "abs(dist[index].value - key) = " << abs(dist[index].value - key) << endl;
	while (low <= hight){
		if (abs(dist[index].value - key) < DELTA) return index;					// found
		if (key < dist[index].value)
			return binarySearch(dist, low, index - 1, key, DELTA);				// go to the left
		if (key > dist[index].value)
			return binarySearch(dist, index + 1, hight, key, DELTA);			// go to the right
	}
	return -1;																	// not found
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Map{
public:
	const int NUM = rand() % 40 + 60;				// the number of the points in range of 60 to 100
	const int ARRAYDIST_SIZE = NUM * (NUM - 1) / 2;	// the number of edges in the complete graph
	Point* pointArray;								// array of points on the map
	Dist* arrayDist;								// the array of distances between points
	
	Map(){										// constructor. create a map of points
		pointArray = new Point[NUM];
		cout << "CREATED AN ARRAY OF " << NUM << " POINTS" << endl;
		for (int i = 0; i < NUM; i++){			// for each point set coordinates
			pointArray[i].setPoint(rand() % 1000 + 1, rand() % 1000 + 1);					
		}
	}

	void getDistance(){									// fill Dist structure with distances between point[i] and point[j] for each i and j
		arrayDist = new Dist[ARRAYDIST_SIZE];			// the number of edges in the complete graph
		int k = 0;										// the index of the element
		for (int i = NUM - 1; i > 0; i--){
			for (int j = 0; j < i; j++){
				arrayDist[k].value = sqrt(				// distance by Pythagorean theorem
					pow(pointArray[i].x - pointArray[j].x, 2) +
					pow(pointArray[i].y - pointArray[j].y, 2));
				arrayDist[k].i = i;						// keep the indexes of the points
				arrayDist[k].j = j;
				k++;		
			}
		}
	}

	void printDistances(){								// print structure Dist
		for (int i = 0; i < NUM * (NUM - 1) / 2; i++){
			cout << arrayDist[i].value << "\t\t" << arrayDist[i].i << " -- " << arrayDist[i].j << endl;
		}
	}

	// calculates parameters of translation of the second map concerning the first on pivotPoints and makes translation for all points of the array of the second map
	void translation(Point pivotFirst, Point pivotSecond, Point* arrayPoint, const int arrayPointSize){
		int dx = pivotFirst.x - pivotSecond.x;		
		int dy = pivotFirst.y - pivotSecond.y;

		for (int i = 0; i < arrayPointSize; i++){
			cout << i << "\t";
			arrayPoint[i].printPoint();
			cout << "\ttransfers to\t";
			arrayPoint[i].x += dx;
			arrayPoint[i].y += dy;
			arrayPoint[i].printPoint();
			cout << endl;
		}
	}

	// calculates the angle of turn of points of the second map around the pole and makes turn for all points of the array of the second map
	void turn(Point P, Point X, Point Y, Point* arrayPoint, const int arrayPointSize){
		// translation of the origin
		X.x -= P.x;
		X.y -= P.y;
		Y.x -= P.x;
		Y.y -= P.y;
		// turn PX and PY before coincidence with Ox
		double alpha = atan((double)(X.y) / (double)(X.x));
		X.turn_angle(alpha);
		Y.turn_angle(alpha);

		// turn PY before coincidence with PX
		double phi;
		if (Y.x > 0) phi = atan((double)Y.y / (double)Y.x);
			else if (Y.x < 0) phi = M_PI - atan((double)Y.y / (double)Y.x);
				else phi = M_PI_2;
		Y.turn_angle(-phi);
		// inverse turn and translation in initial coordinates system
		Y.turn_angle(-alpha);
		Y.x += P.x;
		Y.y += P.y;
		 
		// turn all points except Y
		for (int i = 0; i < arrayPointSize; i++){
			cout << i << "\t";
			arrayPoint[i].printPoint();
			cout << "\tturns to\t";
			
			arrayPoint[i].x -= P.x;
			arrayPoint[i].y -= P.y;
			arrayPoint[i].turn_angle(-phi);
			arrayPoint[i].x += P.x;
			arrayPoint[i].y += P.y;
			
			arrayPoint[i].printPoint();
			cout << endl;
		}
	}

	void search(Map secondMap, const double DELTA){				// find the semelar distances in two maps. The second map must have an ordered array of distances.
		Dist* orderedDist = secondMap.arrayDist;				// ordered array of the distances
		int orderedDistSize = secondMap.ARRAYDIST_SIZE;			// length of the ordered array of the distances

		for (int k = 0; k < this->ARRAYDIST_SIZE; k++){
			// the checked value must belong to the interval between min and max values of the orderedDist
			if (this->arrayDist[k].value > orderedDist[0].value - DELTA &&
				this->arrayDist[k].value < orderedDist[orderedDistSize - 1].value + DELTA)
			{
				int found_index = binarySearch(orderedDist, 0, orderedDistSize - 1, this->arrayDist[k].value, DELTA);		// find value of arrayDist[k] in orderedDist
				if (found_index != -1){			// if it is found
					// print two similar distances
					cout << fixed << setprecision(15) << "Found " << this->arrayDist[k].value << " --> " << orderedDist[found_index].value << endl;
					// pivot points
					// first map
					Point X1 = this->pointArray[this->arrayDist[k].i];
					Point X2 = this->pointArray[this->arrayDist[k].j];
					// second map
					Point Y1 = secondMap.pointArray[orderedDist[found_index].i];
					Point Y2 = secondMap.pointArray[orderedDist[found_index].j];
					
					// print points with these dastances
					cout << "First map" << endl;
						cout << this->arrayDist[k].value << " -- distance between ";
						X1.printPoint();
						cout << " and ";
						X2.printPoint();
						cout << endl;
					cout << "Second map" << endl;
						cout << orderedDist[found_index].value << " -- distance between ";
						Y1.printPoint();
						cout << " and ";
						Y2.printPoint();
						cout << endl;

					// translation and turn
					translation(X1, Y1, secondMap.pointArray, secondMap.NUM);
					cout << endl;
					// update pivot points of the second map
					Y2 = secondMap.pointArray[orderedDist[found_index].j];
					turn(X1, X2, Y2, secondMap.pointArray, secondMap.NUM);
					break;			// after one matching finish searching
				}
			}
			else{
				// cout << "The value " << this->arrayDist[i].value << " doesn't belong to the necessary interval." << endl;
			}
		}

	}
};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

int main(){
	srand(time(NULL));							// initialize random seed -- an integer value to be used by the pseudo-random number generator algorithm
	const double DELTA = 1e-20;						// max deviation between the same points on the two maps

	// create maps
	Map* firstMap = new Map();
	cout << endl;
	Map* secondMap = new Map();

	// fill structure with distances between points
	firstMap->getDistance();
	//firstMap->printDistances();							// show results: print structure of distances
	cout << endl;
	secondMap->getDistance();
	//secondMap->printDistances();						// show results: print structure of distances

	// qsort for the distance values only for the second map
	qsort(secondMap->arrayDist, secondMap->ARRAYDIST_SIZE, sizeof(secondMap->arrayDist[0]), compare_exact);		
	//cout << endl << "The distances of the second map after sorting" << endl;
	//secondMap->printDistances();						// show results: print structure of distances
	
	// search the distance of the first map in the second map
	cout << endl << "Search with DELTA = " << DELTA << endl;
	firstMap->search(*secondMap, DELTA);

	
	system("pause");
	return 0;
}
