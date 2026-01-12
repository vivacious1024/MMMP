
# coding=utf-8
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression
from sklearn.metrics import r2_score, mean_squared_error
import os

def read_excel_file(file_path):
    try:
        if file_path.endswith('.csv'):
            df = pd.read_csv(file_path, index_col=0)
        else:
            df = pd.read_excel(file_path, index_col=0)
        return df
    except Exception as e:
        print(f"Error reading file: {e}")
        return None

def plot_regression(y_true, y_pred, feature_name="Predicted"):
    plot_filename = "/data/data/com.kk.mmmp/cache/LinearRegression_python.png"
    
    plt.figure(figsize=(10, 6))
    
    plt.scatter(y_true, y_pred, color='blue', alpha=0.6)
    plt.plot([y_true.min(), y_true.max()], [y_true.min(), y_true.max()], 'r--', lw=2)
    
    plt.title('Actual vs Predicted')
    plt.xlabel('Actual Values')
    plt.ylabel('Predicted Values')
    plt.tight_layout()
    
    plt.savefig(plot_filename)
    plt.close()
    return plot_filename

def main(file_path):
    df = read_excel_file(file_path)
    if df is None:
        return None, None
        
    df_numeric = df.select_dtypes(include=[np.number])
    if df_numeric.empty or df_numeric.shape[1] < 2:
        return "Need at least 2 numeric columns (X and y)", None
    
    # Assume last column is Target (y), others are Features (X)
    X = df_numeric.iloc[:, :-1].values
    y = df_numeric.iloc[:, -1].values
    
    # Train
    model = LinearRegression()
    model.fit(X, y)
    y_pred = model.predict(X)
    
    # Metrics
    r2 = r2_score(y, y_pred)
    mse = mean_squared_error(y, y_pred)
    
    # Plot
    plot_path = plot_regression(y, y_pred)
    
    # Return metrics as list
    # [R2, MSE, Coefficients...]
    results = [r2, mse]
    results.extend(model.coef_.tolist())
    results.append(model.intercept_)
    
    return results, plot_path
