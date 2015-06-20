#include <iostream>
#include "stdlib.h"
#include "stdio.h"								// time allows to initialize random seed for the pseudo-random number generator algorithm
#include <time.h> 
using namespace std;

struct  Point{									// point on the plane
	int  x;										// coordinates of the point
	int  y;
	
	void setPoint(int x, int y){				// set coordinates x and y to the point
		this->x = x;
		this->y = y;
		cout << "created point (" << x << ", " << y << ")" << endl;
	}
};

struct Dist{									// value keep dictance between point[i] and point[j]
	double value;
	int i;
	int j;
};

int partition(Dist* dist, int low, int hight){						// partition of the array for quicksort
	int l = low, r = hight;											// left and right indexes
	double pivot = dist[(low + hight) / 2].value;					// index of a pivot element in the middle of the array
	while (true){
		while (dist[l].value < pivot) l++;
		while (dist[r].value > pivot) r--;
		if (l < r) {
			swap(dist[l].value, dist[r].value);
			swap(dist[l].i, dist[r].i);
			swap(dist[l].j, dist[r].j);
		}
		else return r;
	}
}

void sort(Dist *dist, int low, int hight){							// quicksort
	if (low < hight){
		int pivot_index = partition(dist, low, hight);				// partition of the array: { < pivot | pivot | > pivot}
		sort(dist, low, pivot_index);								//		recursive call for 
		sort(dist, pivot_index + 1, hight);							//		two parts of the array
	}
}

int binarySearch(Dist* dist, int low, int hight, double key, const double DELTA){		// find the distance in ordered array dist which absolute value differs from the key less then on DELTA
	int index = (low + hight) / 2;
	// cout << "abs(dist[index].value - key) = " << abs(dist[index].value - key) << endl;
	if (abs(dist[index].value - key) < DELTA) return index;					// found
	if (low == hight || low > hight) return -1;											// not found
	if (key < dist[index].value)
		return binarySearch(dist, low, index - 1, key, DELTA);				// go to the left
	if (key > dist[index].value)
		return binarySearch(dist, index + 1, hight, key, DELTA);			// go to the right
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Map{
public:
	const int NUM = 5; // rand() % 40 + 60;			// the number of the points in range of 60 to 100
	const int ARRAYDIST_SIZE = NUM * (NUM - 1) / 2;	// the number of edges in the complete graph
	Point* pointArray;								// array of points on the map
	Dist* arrayDist;								// the array of distances between points
	
	Map(){										// constructor. create a map of points
		pointArray = new Point[NUM];
		cout << "CREATED AN ARRAY OF " << NUM << " POINTS" << endl;
		for (int i = 0; i < NUM; i++){			// for each point set coordinates
			pointArray[i].setPoint(rand() % 100 + 1, rand() % 100 + 1);					
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

	void search(Dist *orderedDist, int orderedDistSize, const double DELTA){
		for (int i = 0; i < this->ARRAYDIST_SIZE; i++){
			// the checked value must belong to the interval between min and max values of the orderedDist
			if (this->arrayDist[i].value > orderedDist[0].value - DELTA &&
				this->arrayDist[i].value < orderedDist[orderedDistSize - 1].value + DELTA){
				int found_index = binarySearch(orderedDist, 0, orderedDistSize - 1, this->arrayDist[i].value, DELTA);
				if (found_index != -1){
					cout << "!!! Found " << this->arrayDist[i].value << " --> " << orderedDist[found_index].value << endl;
				}
			}
			else{
				cout << "The value " << this->arrayDist[i].value << " doesn't belong to the necessary interval." << endl;
			}
		}
		
	}
};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

int main(){
	srand(time(NULL));							// initialize random seed -- an integer value to be used by the pseudo-random number generator algorithm
	const double DELTA = 0.5;						// max deviation between the same points on the two maps

	// create 2 maps
	Map* firstMap = new Map();
	cout << endl;
	Map* secondMap = new Map();

	// fill structure with distances between points
	firstMap->getDistance();
	firstMap->printDistances();							// show results: print structure of distances
	cout << endl;
	secondMap->getDistance();
	secondMap->printDistances();						// show results: print structure of distances

	// sort the distance values only for the second map
	cout << endl << "The distances of the second map after sorting" << endl;
	sort(secondMap->arrayDist, 0, secondMap->ARRAYDIST_SIZE - 1);
	secondMap->printDistances();						// show results: print structure of distances

	// search the distance of the first map in the second map


	//cout << binarySearch(secondMap->arrayDist, 0, secondMap->ARRAYDIST_SIZE - 1, firstMap->arrayDist[0].value, DELTA);
	firstMap->search(secondMap->arrayDist, secondMap->ARRAYDIST_SIZE, DELTA);

	system("pause");
	return 0;
}

