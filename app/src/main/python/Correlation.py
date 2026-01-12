
# coding=utf-8
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
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

def plot_covariance_matrix(df):
    plot_filename = "/data/data/com.kk.mmmp/cache/Covariance_python.png"
    
    # Calculate Covariance Matrix
    cov_matrix = df.cov()
    
    plt.figure(figsize=(10, 8))
    sns.heatmap(cov_matrix, annot=True, fmt=".2f", cmap='coolwarm', cbar=True)
    plt.title('Covariance Matrix Heatmap')
    plt.tight_layout()
    
    plt.savefig(plot_filename)
    plt.close()
    return plot_filename

def main(file_path, method='covariance'):
    df = read_excel_file(file_path)
    if df is None:
        return None, None
    
    # Ensure data is numeric
    df_numeric = df.select_dtypes(include=[np.number])
    
    if df_numeric.empty:
         return "No numeric data found", None

    if method == 'covariance':
        plot_path = plot_covariance_matrix(df_numeric)
        # Return flattened matrix or summary stats
        # Returning the trace (sum of variances) as a simple metric
        return ["Trace: " + str(np.trace(df_numeric.cov()))], plot_path
    else:
        # Default to Correlation if expanded later
        return None, None
