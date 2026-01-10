# coding=utf-8
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, confusion_matrix
import io

def read_data(file_path):
    try:
        # Support both xlsx and csv
        if file_path.endswith('.csv'):
            df = pd.read_csv(file_path)
        else:
            df = pd.read_excel(file_path)
        return df
    except Exception as e:
        print(f"Error reading file: {e}")
        return None

def preprocess_data(df):
    # 1. Handle Missing Values
    # Fill numeric columns with mean
    numeric_cols = df.select_dtypes(include=[np.number]).columns
    df[numeric_cols] = df[numeric_cols].fillna(df[numeric_cols].mean())
    # Fill categorical columns with mode
    for col in df.select_dtypes(include=['object']).columns:
        df[col] = df[col].fillna(df[col].mode()[0])

    # 2. Extract Features (X) and Target (y)
    # Assume the last column is the target variable
    X = df.iloc[:, :-1]
    y = df.iloc[:, -1]

    # 3. Handle Categorical Features in X (One-Hot Encoding)
    X = pd.get_dummies(X, drop_first=True)
    
    return X, y

def split_data(X, y):
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    return X_train, X_test, y_train, y_test

def train_model(X_train, y_train):
    # Increased max_iter for convergence
    model = LogisticRegression(solver='liblinear', max_iter=1000)
    model.fit(X_train, y_train)
    return model

def make_predictions(model, X_test):
    predictions = model.predict(X_test)
    return predictions

def evaluate_model(y_test, predictions):
    accuracy = accuracy_score(y_test, predictions)
    cm = confusion_matrix(y_test, predictions)
    return accuracy, cm

def cross_validation(model, X, y):
    # Handle smaller datasets where cv=5 might fail
    cv = min(5, len(y)//2)
    if cv < 2: cv = 2
    cross_val_scores = cross_val_score(model, X, y, cv=cv)
    return cross_val_scores.mean()

def plot_metrics(accuracy, cross_val_score, cm, labels):
    plot_filename = "/data/data/com.kk.mmmp/cache/Logistic_python.png"
    
    # Create a figure with 2 subplots
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(12, 5))
    
    # Plot 1: Metrics Bar Chart
    metrics = ['Accuracy', 'CV Score']
    scores = [accuracy, cross_val_score]
    ax1.bar(metrics, scores, color=['#4CAF50', '#2196F3'])
    ax1.set_ylim(0, 1.0)
    ax1.set_title('Model Performance')
    for i, v in enumerate(scores):
        ax1.text(i, v + 0.01, f"{v:.2f}", ha='center')

    # Plot 2: Confusion Matrix Heatmap
    sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', ax=ax2, 
                xticklabels=labels, yticklabels=labels)
    ax2.set_xlabel('Predicted')
    ax2.set_ylabel('Actual')
    ax2.set_title('Confusion Matrix')
    
    plt.tight_layout()
    plt.savefig(plot_filename)
    plt.close()
    return plot_filename

def main(file_path):
    df = read_data(file_path)
    if df is None:
        return 0, 0, None
        
    X, y = preprocess_data(df)
    X_train, X_test, y_train, y_test = split_data(X, y)
    
    model = train_model(X_train, y_train)
    predictions = make_predictions(model, X_test)
    
    accuracy, cm = evaluate_model(y_test, predictions)
    cv_score = cross_validation(model, X, y)
    
    unique_labels = np.unique(np.concatenate((y_test, predictions)))
    plot_filename = plot_metrics(accuracy, cv_score, cm, unique_labels)
    
    print(f'Model accuracy: {accuracy:.4f}')
    print(f'Average CV score: {cv_score:.4f}')
    
    return accuracy, cv_score, plot_filename

# 调用主函数
# main(r'F:\Mathmatical_Modeling\Models\test_data\Logistic.xlsx')
