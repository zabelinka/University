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

class Map{
public:
	Point* pointArray;							// array of points on the map
	double** dist;									// array of distances between points
	const double DELTA = 0.5;						// max deviation between the same points on the two maps
	const int NUM = 2; // rand() % 40 + 60;			// the number of the points in range of 60 to 100
	
	Map(){										// constructor. create a map of points
		pointArray = new Point[NUM];
		cout << "created an array of " << NUM << " points" << endl;
		for (int i = 0; i < NUM; i++){			// for each point set coordinates
			pointArray[i].setPoint(rand() % 100 + 1, rand() % 100 + 1);					
		}
	}

	void distance(){
		// create matrix of distances
		dist = new double*[NUM];
		for (int i = 0; i < NUM; i++){
			dist[i] = new double[NUM];
		}
		// fill matrix with distances between points and print it
		for (int i = 0; i < NUM; i++){
			for (int j = 0; j < NUM; j++){
				dist[i][j] = sqrt(										// Pythagorean theorem
					pow(pointArray[i].x - pointArray[j].x, 2) + 
					pow(pointArray[i].y - pointArray[j].y, 2));
				cout << dist[i][j] << "\t";
			}
			cout << endl;
		}
	}
};

int main(){
	srand(time(NULL));							// initialize random seed -- an integer value to be used by the pseudo-random number generator algorithm

	// create 2 maps
	Map* firstMap = new Map();
	cout << endl;
	Map* secondMap = new Map();

	// fill matrix with distances between points
	firstMap->distance();						
	secondMap->distance();


	system("pause");
	return 0;
}

