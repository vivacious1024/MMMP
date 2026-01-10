# coding=utf-8
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

def read_data(file_path):
    # Expect single column time series or dataframe where we take the first numeric column
    try:
        if file_path.endswith('.csv'):
            df = pd.read_csv(file_path)
        else:
            df = pd.read_excel(file_path)
        return df
    except Exception as e:
        print(f"Error reading file: {e}")
        return None

def gm11_predict(x0, num_predict=5):
    """
    GM(1,1) model implementation
    x0: original data sequence (1D numpy array)
    num_predict: number of steps to predict
    """
    n = len(x0)
    # 1. Accumulate (AGO)
    x1 = np.cumsum(x0)
    
    # 2. Construct B and Y matrices
    # z1 is mean of adjacent accumulated data
    z1 = (x1[:n-1] + x1[1:]) / 2.0
    
    B = np.vstack([-z1, np.ones(n-1)]).T
    Y = x0[1:].reshape((n-1, 1))
    
    # 3. Estimate parameters a, b using Least Squares: (B.T * B)^-1 * B.T * Y
    # [[a], [b]]
    try:
        params = np.linalg.inv(B.T @ B) @ B.T @ Y
        a = params[0][0]
        b = params[1][0]
    except np.linalg.LinAlgError:
        return None, None, "Singular Matrix"

    # 4. Predict function
    # x1_pred(k+1) = (x0(1) - b/a) * e^(-ak) + b/a
    def predict_x1(k):
        return (x0[0] - b/a) * np.exp(-a * k) + b/a
    
    # Predict existing range + future
    x1_pred = [predict_x1(k) for k in range(n + num_predict)]
    
    # 5. Inverse Accumulate (IAGO) to get x0_pred
    x1_pred = np.array(x1_pred)
    x0_pred = np.hstack([x0[0], np.diff(x1_pred)])
    
    return x0_pred, (a, b), "Success"

def plot_results(original, predicted):
    plot_filename = "/data/data/com.kk.mmmp/cache/GreyPrediction_python.png"
    
    plt.figure(figsize=(10, 6))
    
    n = len(original)
    m = len(predicted)
    
    plt.plot(range(1, n+1), original, 'o-', label='Original')
    plt.plot(range(1, m+1), predicted, 'x--', label='Predicted (GM(1,1))')
    
    # Highlight the future predictions
    if m > n:
        plt.axvline(x=n, color='gray', linestyle=':', alpha=0.5)
        plt.text(n+0.5, predicted[n], 'Forecast', color='gray')

    plt.title('Grey Prediction Model GM(1,1)')
    plt.xlabel('Time Step')
    plt.ylabel('Value')
    plt.legend()
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    
    plt.savefig(plot_filename)
    plt.close()
    return plot_filename

def calculate_accuracy(original, predicted_part):
    # Relative Error
    residuals = np.abs(original - predicted_part)
    relative_errors = residuals / original
    avg_relative_error = np.mean(relative_errors)
    return avg_relative_error

def main(file_path):
    df = read_data(file_path)
    if df is None:
        return None, None
    
    # Extract data: assume the first numeric column is the time series
    numeric_df = df.select_dtypes(include=[np.number])
    if numeric_df.empty:
        return "No numeric data found", None
        
    data = numeric_df.iloc[:, 0].values
    
    # Run GM(1,1)
    # Predict next 5 steps default
    predicted, params, status = gm11_predict(data, num_predict=5)
    
    if status != "Success":
        print(f"Model Failed: {status}")
        return None, None
    
    # Calculate accuracy on existing data
    existing_pred = predicted[:len(data)]
    avg_error = calculate_accuracy(data, existing_pred)
    
    print(f"GM(1,1) Parameters: a={params[0]:.4f}, b={params[1]:.4f}")
    print(f"Average Relative Error: {avg_error:.2%}")
    
    plot_filename = plot_results(data, predicted)
    
    return predicted.tolist(), plot_filename

# if __name__ == "__main__":
#     main("test.xlsx")
