import seaborn as sns
import matplotlib.pyplot as plt
import pandas as pd

sns.set_theme(style="whitegrid")

data = pd.read_csv("./bench.tsv", sep="\t")

ax = sns.violinplot(x="type", y="time", data=data, hue="format", palette="colorblind")
ax.set(ylim=(0, 0.06))
# for map
#ax.set(ylim=(1, 1.2))

plt.savefig("./result.png")

