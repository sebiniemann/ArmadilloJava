/*******************************************************************************
 * Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Developers:
 *   Sebastian Niemann - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
#include <Expected.hpp>
using armadilloJava::Expected;

#include <iostream>
using std::cout;
using std::endl;

#include <utility>
using std::pair;

#include <armadillo>
using arma::Row;
using arma::Col;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedElemIndFill : public Expected {
    public:
      ExpectedElemIndFill() {
        cout << "Compute ExpectedElemIndFill(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::ElemInd,
            InputClass::Fill
          });

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _elemInd = *static_cast<int*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _fill = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedRowVecElemIndFill();
            expectedColVecElemIndFill();
          }

          cout << "done." << endl;
        }

    protected:
      int _elemInd;
      int _fill;

      void expectedRowVecElemIndFill() {
        cout << "- Compute expectedRowVecAt() ... ";
        Row<double> expected;
        switch (_fill) {
          case 0:
            break;
          case 1:
            expected = Row<double>(_elemInd, arma::fill::none);
            save<double>("Row.elemIndFill", Row<double>(expected));
            break;
          case 2:
            expected = Row<double>(_elemInd, arma::fill::ones);
            save<double>("Row.elemIndFill", Row<double>(expected));
            break;
          case 3:
            expected = Row<double>(_elemInd, arma::fill::randn);
            save<double>("Row.elemIndFill", Row<double>(expected));
            break;
          case 4:
            expected = Row<double>(_elemInd, arma::fill::randu);
            save<double>("Row.elemIndFill", Row<double>(expected));
            break;
          case 5:
            expected = Row<double>(_elemInd, arma::fill::zeros);
            save<double>("Row.elemIndFill", Row<double>(expected));
            break;
        }
        cout << "done." << endl;
      }

  void expectedColVecElemIndFill() {
         cout << "- Compute expectedRowVecAt() ... ";
         Col<double> expected;
         switch (_fill) {
           case 0:
             break;
           case 1:
             expected = Col<double>(_elemInd, arma::fill::none);
             save<double>("Col.elemIndFill", Col<double>(expected));
             break;
           case 2:
             expected = Col<double>(_elemInd, arma::fill::ones);
             save<double>("Col.elemIndFill", Col<double>(expected));
             break;
           case 3:
             expected = Col<double>(_elemInd, arma::fill::randn);
             save<double>("Col.elemIndFill", Col<double>(expected));
             break;
           case 4:
             expected = Col<double>(_elemInd, arma::fill::randu);
             save<double>("Col.elemIndFill", Col<double>(expected));
             break;
           case 5:
             expected = Col<double>(_elemInd, arma::fill::zeros);
             save<double>("Col.elemIndFill", Col<double>(expected));
             break;
         }
         cout << "done." << endl;
       }
  };
}

