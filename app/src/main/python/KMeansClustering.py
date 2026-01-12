
# coding=utf-8
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
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

def plot_clusters(df, labels, centers):
    plot_filename = "/data/data/com.kk.mmmp/cache/KMeans_python.png"
    
    n_features = df.shape[1]
    
    plt.figure(figsize=(10, 6))
    
    if n_features > 2:
        # Use PCA to reduce to 2D for visualization
        pca = PCA(n_components=2)
        components = pca.fit_transform(df)
        plt.scatter(components[:, 0], components[:, 1], c=labels, cmap='viridis', padding=0.5)
        plt.title(f'K-Means Clustering (PCA Reduced, k={len(np.unique(labels))})')
        plt.xlabel('PC1')
        plt.ylabel('PC2')
    elif n_features == 2:
        plt.scatter(df.iloc[:, 0], df.iloc[:, 1], c=labels, cmap='viridis')
        plt.title(f'K-Means Clustering (k={len(np.unique(labels))})')
        plt.xlabel(df.columns[0])
        plt.ylabel(df.columns[1])
    else:
        # 1D
        plt.scatter(df.iloc[:, 0], np.zeros_like(df.iloc[:, 0]), c=labels, cmap='viridis')
        plt.title(f'K-Means Clustering (k={len(np.unique(labels))})')
        plt.xlabel(df.columns[0])
    
    plt.tight_layout()
    plt.savefig(plot_filename)
    plt.close()
    return plot_filename

def main(file_path, n_clusters=3):
    df = read_excel_file(file_path)
    if df is None:
        return None, None
        
    df_numeric = df.select_dtypes(include=[np.number])
    if df_numeric.empty:
        return "No numeric data found", None
        
    # Handle NaNs
    df_numeric = df_numeric.dropna()
    
    # Run K-Means
    kmeans = KMeans(n_clusters=n_clusters, random_state=42, n_init=10)
    labels = kmeans.fit_predict(df_numeric)
    
    # Plot
    plot_path = plot_clusters(df_numeric, labels, kmeans.cluster_centers_)
    
    # Return Inertia (Sum of squared distances of samples to their closest cluster center)
    return [kmeans.inertia_], plot_path
