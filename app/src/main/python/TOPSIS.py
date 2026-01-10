# coding=utf-8
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
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

def normalize_matrix(data):
    # Vector Normalization
    # x_ij = x_ij / sqrt(sum(x_ij^2))
    divisors = np.sqrt(np.sum(data**2, axis=0))
    # Avoid division by zero
    divisors[divisors == 0] = 1
    normalized_data = data / divisors
    return normalized_data

def calculate_ideal_solutions(normalized_data, weights=None):
    if weights is None:
        weights = np.ones(normalized_data.shape[1])
        
    # Weighted Normalized Decision Matrix
    weighted_data = normalized_data * weights
    
    # Ideal Best (Z+) and Ideal Worst (Z-)
    # Assuming all criteria are BENEFIT (Maximization) type for simplicity
    # To support COST type, one would take min for Z+ and max for Z- for those specific columns
    z_plus = np.max(weighted_data, axis=0)
    z_minus = np.min(weighted_data, axis=0)
    
    return weighted_data, z_plus, z_minus

def calculate_distances_and_score(weighted_data, z_plus, z_minus):
    # Euclidean distance
    d_plus = np.sqrt(np.sum((weighted_data - z_plus)**2, axis=1))
    d_minus = np.sqrt(np.sum((weighted_data - z_minus)**2, axis=1))
    
    # Relative Closeness Score (C_i)
    # denominator can be 0 if d_plus + d_minus = 0 -> data points are identical
    denominator = d_plus + d_minus
    denominator[denominator == 0] = 1 
    
    scores = d_minus / denominator
    return scores

def plot_scores(scores, labels):
    plot_filename = "/data/data/com.kk.mmmp/cache/TOPSIS_python.png"
    
    plt.figure(figsize=(10, 6))
    
    # Create a DataFrame for sorting
    df_plot = pd.DataFrame({'Label': labels, 'Score': scores})
    df_plot = df_plot.sort_values(by='Score', ascending=False)
    
    plt.bar(df_plot['Label'].astype(str), df_plot['Score'], color=plt.cm.viridis(np.linspace(0, 1, len(scores))))
    
    plt.title('TOPSIS Ranking Scores')
    plt.xlabel('Alternatives')
    plt.ylabel('Score (Closeness to Ideal)')
    plt.xticks(rotation=45)
    plt.ylim(0, 1.05)
    plt.tight_layout()
    
    plt.savefig(plot_filename)
    plt.close()
    return plot_filename

def main(file_path):
    df = read_excel_file(file_path)
    if df is None:
        return None, None
    
    # Assume first column is Index/Name (already handled by index_col=0 in read)
    # Data is numerical matrix
    data = df.values.astype(float)
    
    # 1. Normalize
    normalized_data = normalize_matrix(data)
    
    # 2. Identify Ideal Solutions
    # Note: Weights are assumed equal here. 
    # In a full system, weights could come from EWM or AHP.
    weighted_data, z_plus, z_minus = calculate_ideal_solutions(normalized_data)
    
    # 3. Calculate Scores
    scores = calculate_distances_and_score(weighted_data, z_plus, z_minus)
    
    # 4. Plot
    # Use index as labels
    labels = df.index
    plot_filename = plot_scores(scores, labels)
    
    # Return sorted results for display
    print("TOPSIS Scores calculated.")
    return scores.tolist(), plot_filename

# if __name__ == "__main__":
#     main("path/to/test.xlsx")
