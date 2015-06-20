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

	void change(Dist another){					// replace the fields of object calling the function with the fields of another object
		double temp_value = this->value;
		int temp_i = this->i;
		int temp_j = this->j;
		this->value = another.value;
		this->i = another.i;
		this->j = another.j;
		another.value = temp_value;
		another.i = temp_i;
		another.j = temp_j;
	}
};

class Map{
public:
	Point* pointArray;							// array of points on the map
	double** dist_matrix;									// array of distances between points
	// not used
	Dist* arrayDist;								// the array of distances between points
	const double DELTA = 0.5;						// max deviation between the same points on the two maps
	const int NUM = 5; // rand() % 40 + 60;			// the number of the points in range of 60 to 100
	
	Map(){										// constructor. create a map of points
		pointArray = new Point[NUM];
		cout << "created an array of " << NUM << " points" << endl;
		for (int i = 0; i < NUM; i++){			// for each point set coordinates
			pointArray[i].setPoint(rand() % 100 + 1, rand() % 100 + 1);					
		}
	}

	// not used
	void distance_matrix(){								// fill matrix with distances between points
		// create matrix of distances
		dist_matrix = new double*[NUM];
		for (int i = 0; i < NUM; i++){
			dist_matrix[i] = new double[NUM];
		}
		// fill matrix with distances between points and print it
		for (int i = 0; i < NUM; i++){
			for (int j = 0; j < NUM; j++){
				dist_matrix[i][j] = sqrt(										// Pythagorean theorem
					pow(pointArray[i].x - pointArray[j].x, 2) + 
					pow(pointArray[i].y - pointArray[j].y, 2));
				cout << dist_matrix[i][j] << "\t";
			}
			cout << endl;
		}
	}

	void distance(){									// fill Dist structure with distances between point[i] and point[j] for each i and j
		arrayDist = new Dist[NUM * (NUM - 1) / 2];		// the number of edges in the complete graph
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
};

int partition(int* a, int low, int hight){						// partition of the array for quicksort
	int l = low, r = hight;										// left and right indexes
	int pivot = a[(low + hight) / 2];							// index of a pivot element in the middle of the array
	while (true){
		cout << "l <= r\t" << l << " <= " << r << endl;
		while (a[l] < pivot) l++;
		while (a[r] > pivot) r--;
		if (l < r) swap(a[l], a[r]);
		else return r;
	}
}

void sort(int *a, int low, int hight){							// quicksort
	cout << "sort " << low << " -- " << hight << endl;
	if (low < hight){
		int pivot = partition(a, low, hight);					// partition of the array: { < pivot | pivot | > pivot}
		sort(a, low, pivot);									//		recursive call for 
		sort(a, pivot + 1, hight);								//		two parts of the array
	}
}

int main(){
	srand(time(NULL));							// initialize random seed -- an integer value to be used by the pseudo-random number generator algorithm

	// create 2 maps
	Map* firstMap = new Map();
	cout << endl;
	Map* secondMap = new Map();

	// fill structure with distances between points
	firstMap->distance();
	firstMap->printDistances();							// show results
	cout << endl;
	secondMap->distance();
	secondMap->printDistances();							// show results

	int a[] = {6, 0, 1, 5, 7, 2, 3, 8, 4, 9};
	sort(a, 0, 9);
	for (int i = 0; i < 10; i++){
		cout << a[i] << "\t";
	}

	

	system("pause");
	return 0;
}

